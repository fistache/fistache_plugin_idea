package seafood.component.parse

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lexer.HtmlHighlightingLexer
import com.intellij.lexer.HtmlLexer
import com.intellij.lexer._HtmlLexer
import com.intellij.psi.xml.XmlTokenType

class SeafoodHighlightingLexer : HtmlHighlightingLexer(), SeafoodHandledLexer {
    init {
        registerHandler(XmlTokenType.XML_NAME, SeafoodLangAttributeHandler())
    }

    /**
     * <script> tag
     */
    override fun findScriptContentProvider(mimeType: String?): HtmlScriptContentProvider? = getScriptTagContentProvider()

    /**
     * <style> tag
     */
    override fun hasSeenStyleTag(): Boolean = seenStyle
    // "lang" attribute
    override fun hasSeenStyleTagLangAttribute() { seenStylesheetType = true }
    override fun getStyleTagLangAttributeValue(): String? = styleType
    override fun getStyleLanguage(): Language? = getStyleTagLanguage(HtmlLexer.ourDefaultStyleLanguage) ?: super.getStyleLanguage()

    /**
     * <script>, <template> and <style> tags
     */
    override fun hasSeenAnyTag(): Boolean = seenTag
    override fun setSeenAnyTag(tag: Boolean) { seenTag = tag }
    override fun isInTagState(): Boolean = (state and HtmlHighlightingLexer.BASE_STATE_MASK) == _HtmlLexer.START_TAG_NAME
}