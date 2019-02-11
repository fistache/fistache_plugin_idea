package fistache.component.lang

import com.intellij.lang.ecmascript6.psi.ES6ExportDefaultAssignment
import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.injection.InjectedLanguageManager
import com.intellij.lang.javascript.JavaScriptSpecificHandlersFactory
import com.intellij.lang.javascript.psi.*
import com.intellij.lang.javascript.psi.ecma6.TypeScriptClassExpression
import com.intellij.lang.javascript.psi.impl.JSReferenceExpressionImpl
import com.intellij.lang.javascript.psi.resolve.JSReferenceExpressionResolver
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiFile
import com.intellij.psi.ResolveResult
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag

class FistacheInjectionSpecificHandlersFactory: JavaScriptSpecificHandlersFactory() {
    override fun createReferenceExpressionResolver(referenceExpression: JSReferenceExpressionImpl?,
                                                   ignorePerformanceLimits: Boolean): ResolveCache.PolyVariantResolver<JSReferenceExpressionImpl> =
            FistacheInjectionReferenceExpressionResolver(referenceExpression, ignorePerformanceLimits)
}

class FistacheInjectionReferenceExpressionResolver(referenceExpression: JSReferenceExpressionImpl?,
                                                   ignorePerformanceLimits: Boolean) :
        JSReferenceExpressionResolver(referenceExpression!!, ignorePerformanceLimits) {
    override fun resolve(ref: JSReferenceExpressionImpl, incompleteCode: Boolean): Array<ResolveResult> =
            resolveInLocalContext(ref) ?:
            resolveInCurrentComponentDefinition(ref) ?:
            arrayOf()

    private fun resolveInLocalContext(ref: JSReferenceExpressionImpl): Array<ResolveResult>? {
        if (ref.qualifier != null) return null

        val injectedLanguageManager = InjectedLanguageManager.getInstance(ref.project)
        // injection host can be xml attribute value or embedded js inside jade tag - this we just skip moving up
        val host = injectedLanguageManager.getInjectionHost(ref)
        val elRef: Ref<PsiElement> = Ref(null)
        PsiTreeUtil.findFirstParent(host ?: ref, Condition {
            if (it is PsiFile) return@Condition true

            val valueElement = (it as? XmlTag)?.getAttribute("@for")?.valueElement ?: return@Condition false
            val forExpression = getCachedForInsideAttribute(valueElement, injectedLanguageManager) ?: return@Condition false
            elRef.set(forExpression.getVarStatement()?.variables?.firstOrNull { it.name == ref.referenceName })
            return@Condition !elRef.isNull
        })
        val foundElement = elRef.get() ?: return null
        return arrayOf(PsiElementResolveResult(foundElement))
    }

    private fun getCachedForInsideAttribute(valueElement: XmlAttributeValue,
                                            injectedLanguageManager: InjectedLanguageManager): FistacheForExpression? {
        return CachedValuesManager.getCachedValue(valueElement, CachedValueProvider {
            var forExpression = PsiTreeUtil.findChildOfType(valueElement, FistacheForExpression::class.java)
            if (forExpression == null) {
                var lookForInjectedInside: PsiElement = valueElement
                if (HTMLLanguage.INSTANCE != valueElement.language) {
                    val embeddedJS = PsiTreeUtil.findChildOfType(valueElement, JSEmbeddedContent::class.java)
                    val literal = embeddedJS?.firstChild as? JSLiteralExpression
                    if (literal != null) {
                        lookForInjectedInside = literal
                    }
                }
                forExpression = injectedLanguageManager.getInjectedPsiFiles(lookForInjectedInside)
                        ?.map { PsiTreeUtil.findChildOfType(it.first, FistacheForExpression::class.java) }?.firstOrNull()
            }
            return@CachedValueProvider CachedValueProvider.Result(forExpression, valueElement)
        })
    }

    private fun resolveInCurrentComponentDefinition(ref: JSReferenceExpression): Array<ResolveResult>? {
        ref.referenceName ?: return null

        when {
            ref.firstChild is JSThisExpression -> {
                val defaultExport = findScriptWithExport(ref)?.second?.stubSafeElement as? TypeScriptClassExpression ?: return null
                val field = defaultExport.fields.firstOrNull { it.name == ref.referenceName } ?: return null

                return arrayOf(PsiElementResolveResult(field))
            }
            ref.firstChild is JSReferenceExpression -> {
                // todo: resolve object props
                return arrayOf()
            }
            else -> return null
        }
    }
}

fun findScriptWithExport(element: PsiElement) : Pair<PsiElement, ES6ExportDefaultAssignment>? {
    val xmlFile = getContainingXmlFile(element) ?: return null

    val module = fistache.component.codeInsight.findModule(xmlFile) ?: return null
    val defaultExport = com.intellij.lang.ecmascript6.resolve.ES6PsiUtil.findDefaultExport(module)
            as? ES6ExportDefaultAssignment ?: return null
    if (defaultExport.stubSafeElement is TypeScriptClassExpression) {
        return Pair(module, defaultExport)
    }
    return null
}

fun getContainingXmlFile(element: PsiElement): XmlFile? =
        (element.containingFile as? XmlFile ?: element as? XmlFile ?:
        InjectedLanguageManager.getInstance(element.project).getInjectionHost(element)?.containingFile as? XmlFile)
