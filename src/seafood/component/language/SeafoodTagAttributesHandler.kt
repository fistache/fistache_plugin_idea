package seafood.component.language

import com.intellij.lexer.BaseHtmlLexer
import com.intellij.lexer.Lexer

class SeafoodTagAttributesHandler : BaseHtmlLexer.TokenHandler {
    override fun handleElement(lexer: Lexer?) {
        val handled = lexer as SeafoodHandledLexer
        if (!handled.inTagState()) {
            val text = lexer.tokenText
            handled.setSeenSeafoodAttribute(text.startsWith(":") || text.startsWith("@"))
        }
    }
}
