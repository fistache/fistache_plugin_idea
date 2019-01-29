package seafood.component.lang

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lexer.*
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.xml.XmlTokenType
import seafood.component.lang.handlers.*
import seafood.component.lang.lexer._SeafoodLexer

class SeafoodLexer : HtmlLexer(MergingLexerAdapter(FlexAdapter(_SeafoodLexer()), TokenSet.create(XmlTokenType.XML_COMMENT_CHARACTERS, XmlTokenType.XML_WHITE_SPACE, XmlTokenType.XML_REAL_WHITE_SPACE,
        XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN, XmlTokenType.XML_DATA_CHARACTERS,
        XmlTokenType.XML_TAG_CHARACTERS)), true), SeafoodHandledLexer {
    private var seenTemplate: Boolean = false
    private var seenAttributeExpression: Boolean = false

    companion object {
        const val SEEN_ATTRIBUTE_EXPRESSION: Int = 0x10000
    }

    init {
        registerHandler(XmlTokenType.XML_NAME, SeafoodLangAttributeHandler())
        registerHandler(XmlTokenType.XML_NAME, SeafoodTagOpenHandler())
        registerHandler(XmlTokenType.XML_ATTRIBUTE_VALUE_START_DELIMITER, SeafoodAttributeStartDelimiterHandler())

        val scriptCleaner = SeafoodTemplateCleaner()
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
    override fun getStyleLanguage(): Language? = getStyleTagLanguage(HtmlLexer.ourDefaultStyleLanguage) ?: super.getStyleLanguage()

    /**
     * <script>, <template> and <style> tags
     */
    override fun setSeenTemplate(template:Boolean) { seenTemplate = template }
    override fun seenTemplate(): Boolean = seenTemplate
    override fun seenTag(): Boolean = seenTag
    override fun setSeenTag(tag: Boolean) { seenTag = tag }
    override fun inTagState(): Boolean = state and HtmlHighlightingLexer.BASE_STATE_MASK == _HtmlLexer.START_TAG_NAME
    override fun seenAttribute(): Boolean = seenAttribute
    override fun setSeenAttribute(attribute: Boolean) { seenAttribute = attribute }
    override fun seenAttributeExpression() = seenAttributeExpression
    override fun setSeenAttributeExpression(value: Boolean) { seenAttributeExpression = value }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        seenTemplate = initialState and SeafoodTagOpenHandler.SEEN_TEMPLATE != 0
        seenAttributeExpression = initialState and SEEN_ATTRIBUTE_EXPRESSION != 0
        super.start(buffer, startOffset, endOffset, initialState)
    }

    override fun getState(): Int {
        val state = super.getState()
        return state or
                (when {
                    seenTemplate -> SeafoodTagOpenHandler.SEEN_TEMPLATE
                    seenAttributeExpression -> SeafoodLexer.SEEN_ATTRIBUTE_EXPRESSION
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

        if (seenAttributeExpression && type == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN) {
            return SeafoodElementTypes.EMBEDDED_TS
        }

        return type
    }
}
