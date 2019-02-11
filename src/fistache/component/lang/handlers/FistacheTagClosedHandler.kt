package fistache.component.lang.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
//import fistache.component.lang.FistacheHandledLexer

class FistacheTagClosedHandler : BaseHtmlLexer.TokenHandler{
    override fun handleElement(lexer: Lexer?) {
//        val handled = lexer as FistacheHandledLexer
//        if (handled.seenTemplate() && handled.seenScript()) {
//            handled.setSeenTag(true)
//        }
    }
}
