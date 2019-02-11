package fistache.component.lang.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import fistache.component.lang.FistacheHandledLexer

class FistacheAttributeStartDelimiterHandler : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer) {
        val handled = lexer as FistacheHandledLexer
        if (!handled.seenTag()) {
            val text = lexer.tokenText
            handled.setSeenAttributeExpression(text.startsWith("{"))
        }
    }
}
