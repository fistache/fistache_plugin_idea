package seafood.component.lang.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import seafood.component.lang.SeafoodHandledLexer

class SeafoodAttributeStartDelimiterHandler : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer) {
        val handled = lexer as SeafoodHandledLexer
        if (!handled.seenTag()) {
            val text = lexer.tokenText
            handled.setSeenAttributeExpression(text.startsWith("{"))
        }
    }
}
