package seafood.component.parse

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lang.typescript.TypeScriptContentProvider

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

    fun styleViaLang(default: Language?): Language? = styleViaLang(default, getStyleType())

    fun findScriptContentProviderSeafood(): HtmlScriptContentProvider? {
        return object: TypeScriptContentProvider() {}
    }

    companion object {
        fun styleViaLang(default: Language?, style: String?): Language? {
            if (default != null && style != null) {
                default.dialects
                        .filter { style.equals(it.id, ignoreCase = true) }
                        .forEach { return it }
            }
            return if (style == null) Language.findLanguageByID("Stylus") else null
        }
    }
}