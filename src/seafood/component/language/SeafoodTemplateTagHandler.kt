package seafood.component.language

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import seafood.component.parse.SeafoodHandledLexer

class SeafoodTemplateTagHandler : BaseHtmlLexer.TokenHandler {
    companion object {
        const val SEEN_TEMPLATE: Int = 0x2000
    }

    override fun handleElement(lexer: Lexer?) {
        val handled = lexer as SeafoodHandledLexer
        if (!handled.seenTag() && handled.inTagState() && "template" == lexer.tokenText) {
            handled.setSeenTemplate(true)
        }
        if (!handled.inTagState() && "template" == lexer.tokenText) {
            handled.setSeenTag(false)
        }
    }
}