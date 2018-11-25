package seafood.component.language

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.javascript.types.JSEmbeddedContentElementType
import com.intellij.lang.javascript.types.JSFileElementType
import com.intellij.lang.typescript.TypeScriptContentProvider
import com.intellij.lexer.Lexer
import com.intellij.psi.tree.IFileElementType
import seafood.component.language.expressions.SeafoodTemplateForExpression

object SeafoodElementTypes {
    val FILE: IFileElementType = JSFileElementType.create(SeafoodTemplateLanguage.INSTANCE)
    val EMBEDDED_TS: JSEmbeddedContentElementType = object : JSEmbeddedContentElementType(SeafoodTemplateLanguage.INSTANCE, "SeafoodTemplate") {
        override fun createStripperLexer(baseLanguage: Language): Lexer? = TypeScriptContentProvider().highlightingLexer
    }
    val V_FOR_EXPRESSION: SeafoodTemplateCompositeElementType = object : SeafoodTemplateCompositeElementType("V_FOR_EXPRESSION") {
        override fun createCompositeNode(): ASTNode = SeafoodTemplateForExpression(this)
    }
}