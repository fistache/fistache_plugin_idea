package fistache.component.lang.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import fistache.component.lang.FistacheHandledLexer

class FistacheTemplateCleaner : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer?) {
        (lexer as FistacheHandledLexer).setSeenTemplate(false)
    }
}
