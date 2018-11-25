package seafood.component.language

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lang.LanguageHtmlScriptContentProvider
import com.intellij.lang.javascript.JSElementTypes.*
import com.intellij.lang.javascript.JavaScriptHighlightingLexer
import com.intellij.lang.javascript.TypeScriptFileType
import com.intellij.lang.javascript.dialects.JSLanguageLevel
import com.intellij.lang.javascript.dialects.TypeScriptLanguageDialect
import com.intellij.lang.javascript.highlighting.TypeScriptHighlighter
import com.intellij.lang.typescript.TypeScriptContentProvider
import com.intellij.lang.typescript.TypeScriptElementTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.psi.tree.IElementType

interface SeafoodHandledLexer {
    fun seenScript():Boolean
    fun setSeenScript()
    fun setSeenScriptType()
    fun seenTemplate(): Boolean
    fun setSeenTemplate(template: Boolean)
    fun seenStyle():Boolean
    fun setSeenStyleType()
    fun seenTag():Boolean
    fun setSeenTag(tag:Boolean)
    fun inTagState():Boolean
    fun seenAttribute():Boolean
    fun setSeenAttribute(attribute:Boolean)
    fun seenSeafoodAttribute(): Boolean
    fun setSeenSeafoodAttribute(value: Boolean)
    fun getScriptType(): String?
    fun getStyleType(): String?

    fun styleViaLang(): Language? = Language.findLanguageByID("SCSS")

    fun findScriptContentProviderSeafood(): HtmlScriptContentProvider? {
        return object: HtmlScriptContentProvider {
            override fun getScriptElementType(): IElementType = TS_EMBEDDED_CONTENT_MODULE
            override fun getHighlightingLexer(): Lexer? = TypeScriptContentProvider().highlightingLexer
        }
    }
}