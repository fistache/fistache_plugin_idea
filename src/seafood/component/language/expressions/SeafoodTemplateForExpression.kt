package seafood.component.language.expressions

import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.lang.javascript.psi.JSParenthesizedExpression
import com.intellij.lang.javascript.psi.JSReferenceExpression
import com.intellij.lang.javascript.psi.JSVarStatement
import com.intellij.lang.javascript.psi.impl.JSExpressionImpl

class SeafoodTemplateForExpression(templateElementType: IElementType) : JSExpressionImpl(templateElementType) {
    fun getVarStatement(): JSVarStatement? {
        if (firstChild is JSVarStatement) return firstChild as JSVarStatement
        if (firstChild is JSParenthesizedExpression) {
            return PsiTreeUtil.findChildOfType(firstChild, JSVarStatement::class.java)
        }
        return null
    }
    fun getReferenceExpression(): PsiElement? = children.firstOrNull { it is JSReferenceExpression }
}