package fistache.component.lang.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import fistache.component.lang.FistacheHandledLexer

class FistacheLangAttributeHandler : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer) {
        val handled = lexer as FistacheHandledLexer
        val seenStyle = handled.seenStyle()
        if (!handled.seenTag() && !handled.inTagState()) {
            if (seenStyle) {
                if ("lang" == lexer.tokenText) {
                    handled.seenStyle()
                }
            }
        }
    }
}
