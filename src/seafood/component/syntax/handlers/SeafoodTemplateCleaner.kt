package seafood.component.parse.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import seafood.component.parse.SeafoodHandledLexer

class SeafoodTemplateCleaner : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer?) {
        (lexer as SeafoodHandledLexer).setSeenTemplate(false)
    }
}
