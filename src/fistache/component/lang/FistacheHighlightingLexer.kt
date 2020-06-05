package fistache.component.lang

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lang.css.CSSLanguage
import com.intellij.lexer.*
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.xml.XmlTokenType
import fistache.component.lang.handlers.*

class FistacheHighlightingLexer : HtmlHighlightingLexer(MergingLexerAdapter(FlexAdapter(fistache.component.lang.lexer._FistacheLexer()), TokenSet.create(XmlTokenType.XML_COMMENT_CHARACTERS, XmlTokenType.XML_WHITE_SPACE, XmlTokenType.XML_REAL_WHITE_SPACE,
        XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN, XmlTokenType.XML_DATA_CHARACTERS,
        XmlTokenType.XML_TAG_CHARACTERS)), true, null), FistacheHandledLexer {
    private var seenTemplate: Boolean = false
    private var seenAttributeExpression: Boolean = false

    init {
        registerHandler(XmlTokenType.XML_NAME, FistacheLangAttributeHandler())
        registerHandler(XmlTokenType.XML_NAME, FistacheTagOpenHandler())
        registerHandler(XmlTokenType.XML_ATTRIBUTE_VALUE_START_DELIMITER, FistacheAttributeStartDelimiterHandler())

        val scriptCleaner = FistacheTemplateCleaner()
        registerHandler(XmlTokenType.XML_END_TAG_START, scriptCleaner)
        registerHandler(XmlTokenType.XML_EMPTY_ELEMENT_END, scriptCleaner)
    }

    /**
     * <script> tag
     */
    override fun seenScript(): Boolean = seenScript
    override fun setSeenScript() { seenScript = true }
    override fun getScriptType(): String? = scriptType
    override fun setSeenScriptType() { seenContentType = true }
    override fun findScriptContentProvider(mimeType: String?): HtmlScriptContentProvider? = getScriptTagContentProvider()

    /**
     * <style> tag
     */
    override fun seenStyle(): Boolean = seenStyle
    override fun setSeenStyleType() { seenStylesheetType = true }
    override fun getStyleType(): String? = styleType
    override fun getStyleLanguage(): Language? = getStyleTagLanguage(CSSLanguage.INSTANCE) ?: super.getStyleLanguage()

    /**
     * <script>, <template> and <style> tags
     */
    override fun setSeenTemplate(template:Boolean) { seenTemplate = template }
    override fun seenTemplate(): Boolean = seenTemplate
    override fun seenTag(): Boolean = seenTag
    override fun setSeenTag(tag: Boolean) { seenTag = tag }
    override fun inTagState(): Boolean = baseState() == _HtmlLexer.START_TAG_NAME
    override fun seenAttribute(): Boolean = seenAttribute
    override fun setSeenAttribute(attribute: Boolean) { seenAttribute = attribute }
    override fun seenAttributeExpression() = seenAttributeExpression
    override fun setSeenAttributeExpression(value: Boolean) { seenAttributeExpression = value }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        seenTemplate = initialState and FistacheTagOpenHandler.SEEN_TEMPLATE != 0
        seenAttributeExpression = initialState and FistacheLexer.SEEN_ATTRIBUTE_EXPRESSION != 0
        super.start(buffer, startOffset, endOffset, initialState)
    }

    override fun getState(): Int {
        val state = super.getState()
        return state or
                (when {
                    seenTemplate -> FistacheTagOpenHandler.SEEN_TEMPLATE
                    seenAttributeExpression -> FistacheLexer.SEEN_ATTRIBUTE_EXPRESSION
                    else -> 0
                })
    }

    override fun endOfTheEmbeddment(name:String?):Boolean {
        return super.endOfTheEmbeddment(name) ||
                seenTemplate && "template" == name
    }

    override fun getTokenType(): IElementType? {
        if (seenTemplate && "html".equals(scriptType, true)) {
            seenContentType = false
            scriptType = null
            seenScript = false
        }

        val type = super.getTokenType()
        if (type == XmlTokenType.TAG_WHITE_SPACE && baseState() == 0) return XmlTokenType.XML_REAL_WHITE_SPACE
        if (seenAttributeExpression && type == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN) return FistacheElementTypes.EMBEDDED_TS
        return type
    }

    private fun baseState() = state and BASE_STATE_MASK
}