package seafood.component.parse

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.javascript.psi.JSParenthesizedExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.JSVarStatement
import com.intellij.lang.javascript.psi.impl.JSExpressionImpl
import com.intellij.lang.javascript.types.JSFileElementType
import com.intellij.lang.javascript.types.TypeScriptEmbeddedContentElementType
import com.intellij.lexer.Lexer
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.ICompositeElementType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.util.PsiTreeUtil

object SeafoodElementTypes {
    val FILE: IFileElementType = JSFileElementType.create(SeafoodInjectionLanguage.INSTANCE)
    val EMBEDDED_TS: TypeScriptEmbeddedContentElementType = object : TypeScriptEmbeddedContentElementType(SeafoodInjectionLanguage.INSTANCE, "SeafoodInjection") {
        override fun createStripperLexer(baseLanguage: Language): Lexer? = null
    }
    val FOR_EXPRESSION: SeafoodInjectionCompositeElementType = object : SeafoodInjectionCompositeElementType("FOR_EXPRESSION") {
        override fun createCompositeNode(): ASTNode = SeafoodForExpression(this)
    }
}

abstract class SeafoodInjectionCompositeElementType(debugName: String) : IElementType(debugName, SeafoodInjectionLanguage.INSTANCE), ICompositeElementType

class SeafoodForExpression(elementType: IElementType) : JSExpressionImpl(elementType) {
    fun getVarStatement(): JSVarStatement? {
        if (firstChild is JSVarStatement) return firstChild as JSVarStatement
        if (firstChild is JSParenthesizedExpression) {
            return PsiTreeUtil.findChildOfType(firstChild, JSVarStatement::class.java)
        }
        return null
    }
    fun getReferenceExpression(): PsiElement? = children.firstOrNull { it is JSReferenceExpression }
}
