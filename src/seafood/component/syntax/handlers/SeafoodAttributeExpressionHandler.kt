package seafood.component.syntax.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import seafood.component.syntax.SeafoodHandledLexer

class SeafoodAttributeExpressionHandler : BaseHtmlLexer.TokenHandler{
    override fun handleElement(lexer: Lexer?) {
        val handled = lexer as SeafoodHandledLexer
        if (!handled.seenTag()) {
            val text = lexer.tokenText
            handled.setSeenAttributeExpression(text.startsWith(":") || text.startsWith("@"))
        }
    }
}
