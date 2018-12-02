package seafood.component.parse

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lang.typescript.TypeScriptContentProvider

interface SeafoodHandledLexer {
    /**
     * <script> tag
     */
    fun getScriptTagContentProvider(): HtmlScriptContentProvider? = TypeScriptContentProvider()

    /**
     * <style> tag
     */
    fun hasSeenStyleTag(): Boolean
    // "lang" attribute
    fun hasSeenStyleTagLangAttribute()
    fun getStyleTagLangAttributeValue(): String?
    fun getStyleTagLanguage(default: Language?): Language? = SeafoodHandledLexer.getStyleTagLanguage(default, getStyleTagLangAttributeValue())

    /**
     * <script>, <template> and <style> tags
     */
    fun hasSeenAnyTag(): Boolean
    fun setSeenAnyTag(tag: Boolean)
    fun isInTagState(): Boolean

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