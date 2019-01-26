package seafood.component.parse.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import seafood.component.parse.SeafoodHandledLexer

class SeafoodLangAttributeHandler : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer) {
        val handled = lexer as SeafoodHandledLexer
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
