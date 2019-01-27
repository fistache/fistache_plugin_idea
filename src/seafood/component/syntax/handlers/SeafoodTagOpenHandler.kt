package seafood.component.syntax.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import seafood.component.syntax.SeafoodHandledLexer

class SeafoodTagOpenHandler: BaseHtmlLexer.TokenHandler {
    companion object {
        const val SEEN_TEMPLATE: Int = 0x2000
    }

    override fun handleElement(lexer: Lexer?) {
        val handled = lexer as SeafoodHandledLexer
        if (!handled.seenTag() && handled.inTagState()) {
             handled.setSeenTemplate(true)
        }
        if (!handled.inTagState()) {
            handled.setSeenTag(false)
        }
    }
}
