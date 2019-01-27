package seafood.component.syntax.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import seafood.component.syntax.SeafoodHandledLexer

class SeafoodTemplateCleaner : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer?) {
        (lexer as SeafoodHandledLexer).setSeenTemplate(false)
    }
}
