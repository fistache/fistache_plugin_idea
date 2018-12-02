package seafood.component.parse

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer

class SeafoodLangAttributeHandler : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer) {
        val handled = lexer as SeafoodHandledLexer
        val seenStyle = handled.hasSeenStyleTag()
        if (!handled.hasSeenAnyTag() && !handled.isInTagState()) {
            if (seenStyle) {
                if ("lang" == lexer.tokenText) {
                    handled.hasSeenStyleTagLangAttribute()
                }
            }
        }
    }
}
