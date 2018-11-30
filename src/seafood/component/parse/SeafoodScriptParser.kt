package seafood.component.parse

import com.intellij.psi.tree.IElementType
import com.intellij.lang.PsiBuilder
import com.intellij.lang.javascript.JSElementTypes
import com.intellij.lang.javascript.ecmascript6.parsing.TypeScriptParser
import com.intellij.lang.javascript.psi.JSEmbeddedContent
import com.intellij.lexer.EmbeddedTokenTypesProvider

class SeafoodScriptParser(builder: PsiBuilder) : TypeScriptParser(builder) {
    fun parseSeafood(root: IElementType) {
        val rootMarker = builder.mark()
        while (!builder.eof()) {
            if (builder.tokenType == JSElementTypes.TS_EMBEDDED_CONTENT) {
                this.parseJS(JSElementTypes.TS_EMBEDDED_CONTENT)
            } else {
                builder.advanceLexer()
            }
        }
        rootMarker.done(root)
    }

//    private fun parseScript(builder: PsiBuilder) : Boolean {
//        this.parseJS(builder.)
//        builder.advanceLexer()
////        if (!myExpressionParser.parseExpressionOptional()) {
////            builder.error(JSBundle.message("javascript.parser.message.expected.expression"))
////            builder.advanceLexer()
////            return false
////        }
////        return true
//    }
}