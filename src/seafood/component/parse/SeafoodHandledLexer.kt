package seafood.component.parse

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lang.typescript.TypeScriptContentProvider

interface SeafoodHandledLexer {
    /**
     * <script> tag
     */
    fun seenScript(): Boolean
    fun setSeenScript()
    fun setSeenScriptType()
    fun getScriptTagContentProvider(): HtmlScriptContentProvider? = TypeScriptContentProvider()

    /**
     * <template> tag
     */
    fun seenTemplate(): Boolean
    fun setSeenTemplate(template: Boolean)
    fun getScriptType(): String?

    /**
     * <style> tag
     */
    fun seenStyle(): Boolean
    fun setSeenStyleType()
    fun getStyleType(): String?
    fun getStyleTagLanguage(default: Language?): Language? = SeafoodHandledLexer.getStyleTagLanguage(default, getStyleType())

    fun seenTag(): Boolean
    fun setSeenTag(tag: Boolean)
    fun inTagState(): Boolean
    fun seenAttribute(): Boolean
    fun setSeenAttribute(attribute: Boolean)
    fun seenAttributeExpression(): Boolean
    fun setSeenAttributeExpression(value: Boolean)

    companion object {
        fun getStyleTagLanguage(default: Language?, style: String?): Language? {
            if (default != null && style != null) {
                default.dialects
                        .filter { style.equals(it.id, ignoreCase = true) }
                        .forEach { return it }
            }
            return if (style == null) Language.findLanguageByID("Stylus") else null
        }
    }
}