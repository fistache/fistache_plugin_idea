package seafood.component.codeInsight

import com.intellij.lang.ASTNode
import com.intellij.lang.ecmascript6.psi.ES6ExportDefaultAssignment
import com.intellij.lang.ecmascript6.psi.ES6FunctionProperty
import com.intellij.lang.ecmascript6.psi.JSClassExpression
import com.intellij.lang.ecmascript6.psi.JSExportAssignment
import com.intellij.lang.ecmascript6.resolve.JSFileReferencesUtil
import com.intellij.lang.javascript.*
import com.intellij.lang.javascript.index.FrameworkIndexingHandler
import com.intellij.lang.javascript.index.JSSymbolUtil
import com.intellij.lang.javascript.psi.*
import com.intellij.lang.javascript.psi.ecma6.ES6Decorator
import com.intellij.lang.javascript.psi.ecmal4.JSAttributeList
import com.intellij.lang.javascript.psi.impl.JSPsiImplUtils
import com.intellij.lang.javascript.psi.resolve.JSEvaluateContext
import com.intellij.lang.javascript.psi.resolve.JSResolveUtil
import com.intellij.lang.javascript.psi.resolve.JSTypeEvaluator
import com.intellij.lang.javascript.psi.resolve.JSTypeInfo
import com.intellij.lang.javascript.psi.stubs.JSElementIndexingData
import com.intellij.lang.javascript.psi.stubs.JSImplicitElementStructure
import com.intellij.lang.javascript.psi.stubs.impl.JSElementIndexingDataImpl
import com.intellij.lang.javascript.psi.types.*
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.impl.source.tree.TreeUtil
import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlDocument
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.util.HtmlUtil
import seafood.component.SeafoodFileType

class SeafoodFrameworkHandler : FrameworkIndexingHandler()

fun findModule(element: PsiElement?): JSEmbeddedContent? {
    val file = element as? XmlFile ?: element?.containingFile as? XmlFile
    if (file != null && file.fileType == SeafoodFileType.INSTANCE) {
        if (file is PsiFileImpl) {
            val greenStub = file.greenStub
            //stub-safe path
            if (greenStub != null) {
                val children = greenStub.getChildrenByType<JSElement>(JSExtendedLanguagesTokenSetProvider.MODULE_EMBEDDED_CONTENTS,
                        JSEmbeddedContent.ARRAY_FACTORY)
                val result = children.firstOrNull()
                return if (result is JSEmbeddedContent) result else null
            }
        }
        val script = findScriptTag(file)
        if (script != null) {
            return PsiTreeUtil.findChildOfType(script, JSEmbeddedContent::class.java)
        }
    }
    return null
}

fun findScriptTag(xmlFile: XmlFile): XmlTag? {
    if (xmlFile.fileType == SeafoodFileType.INSTANCE) {
        val visitor = MyScriptVisitor()
        xmlFile.accept(visitor)
        return visitor.scriptTag
    }
    return null
}

private class MyScriptVisitor : SeafoodFileVisitor() {
    internal var jsElement: JSEmbeddedContent? = null
    internal var scriptTag: XmlTag? = null

    override fun visitXmlTag(tag: XmlTag?) {
        if (HtmlUtil.isScriptTag(tag)) {
            scriptTag = tag
            jsElement = PsiTreeUtil.findChildOfType(tag, JSEmbeddedContent::class.java)
        }
    }
}

open class SeafoodFileVisitor : XmlElementVisitor() {
    override fun visitXmlDocument(document: XmlDocument?): Unit = recursion(document)

    override fun visitXmlFile(file: XmlFile?): Unit = recursion(file)

    protected fun recursion(element: PsiElement?) {
        element?.children?.forEach { it.accept(this) }
    }
}
