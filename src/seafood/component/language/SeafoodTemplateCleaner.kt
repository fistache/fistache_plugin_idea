package seafood.component.language

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer

class SeafoodTemplateCleaner : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer?) {
        (lexer as SeafoodHandledLexer).setSeenTemplate(false)
    }
}