package fistache.component.lang

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.JSVarStatement
import com.intellij.lang.javascript.psi.JSVariable
import com.intellij.lang.javascript.psi.impl.JSExpressionImpl
import com.intellij.lang.javascript.psi.impl.JSVariableImpl
import com.intellij.lang.javascript.psi.stubs.JSVariableStubBase
import com.intellij.lang.javascript.types.JSVariableElementType
import com.intellij.lang.javascript.types.TypeScriptEmbeddedContentElementType
import com.intellij.lexer.Lexer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.ICompositeElementType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlTag

object FistacheElementTypes {
    val EMBEDDED_TS: TypeScriptEmbeddedContentElementType = object : TypeScriptEmbeddedContentElementType(FistacheInjectionLanguage.INSTANCE, "FistacheInjection") {
        override fun createStripperLexer(baseLanguage: Language): Lexer? = null
    }

    val FOR_EXPRESSION: FistacheInjectionCompositeElementType = object : FistacheInjectionCompositeElementType("FOR_EXPRESSION") {
        override fun createCompositeNode(): ASTNode = FistacheForExpression(this)
    }

    val FOR_VARIABLE: JSVariableElementType = object : JSVariableElementType("FOR_VARIABLE") {
        override fun construct(node: ASTNode?): PsiElement? {
            return FistacheForVariable(node)
        }

        override fun shouldCreateStub(node: ASTNode?): Boolean {
            return false
        }
    }
}

abstract class FistacheInjectionCompositeElementType(debugName: String) : IElementType(debugName, FistacheInjectionLanguage.INSTANCE), ICompositeElementType

class FistacheForExpression(elementType: IElementType) : JSExpressionImpl(elementType) {
    fun getVarStatement(): JSVarStatement? = children.firstOrNull { it is JSVarStatement } as JSVarStatement
    fun getReferenceExpression(): PsiElement? = children.firstOrNull { it is JSReferenceExpression }
}

class FistacheForVariable(node: ASTNode?) : JSVariableImpl<JSVariableStubBase<JSVariable>, JSVariable>(node) {
    override fun hasBlockScope(): Boolean = true

    override fun getDeclarationScope(): PsiElement? =
            PsiTreeUtil.getContextOfType(this, XmlTag::class.java, PsiFile::class.java)
}
