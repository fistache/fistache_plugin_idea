package seafood.component.parse.handlers

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer
import seafood.component.parse.SeafoodHandledLexer

class SeafoodAttributeExpression : BaseHtmlLexer.TokenHandler{
    override fun handleElement(lexer: Lexer?) {
        val handled = lexer as SeafoodHandledLexer
        if (!handled.hasSeenAnyTag()) {
            val text = lexer.tokenText
            handled.setSeenAnyTag(text.startsWith(":") || text.startsWith("@") || text.startsWith("v-"))
        }
    }
}
