package fistache.component.lang.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import fistache.component.lang.FistacheHandledLexer

class FistacheTagOpenHandler: BaseHtmlLexer.TokenHandler {
    companion object {
        const val SEEN_TEMPLATE: Int = 0x2000
    }

    override fun handleElement(lexer: Lexer?) {
        val handled = lexer as FistacheHandledLexer
        if (!handled.seenTag() && handled.inTagState()) {
             handled.setSeenTemplate(true)
        }
        if (!handled.inTagState()) {
            handled.setSeenTag(false)
        }
    }
}
