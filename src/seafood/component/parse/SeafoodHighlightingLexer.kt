package seafood.component.parse

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lang.javascript.dialects.JSLanguageLevel
import com.intellij.lexer.HtmlHighlightingLexer
import com.intellij.lexer._HtmlLexer
import com.intellij.psi.tree.IElementType
import com.intellij.psi.xml.XmlTokenType
import org.jetbrains.vuejs.language.SeafoodLangAttributeHandler
import seafood.component.language.*

class SeafoodHighlightingLexer : HtmlHighlightingLexer(), SeafoodHandledLexer {
    private var seenTemplate:Boolean = false
    private var seenSeafoodAttribute: Boolean = false

    init {
        registerHandler(XmlTokenType.XML_NAME, SeafoodLangAttributeHandler())
        registerHandler(XmlTokenType.XML_NAME, SeafoodTemplateTagHandler())
        registerHandler(XmlTokenType.XML_NAME, SeafoodTagAttributesHandler())
        registerHandler(XmlTokenType.XML_TAG_END, SeafoodTagClosedHandler())
        val scriptCleaner = SeafoodTemplateCleaner()
        registerHandler(XmlTokenType.XML_END_TAG_START, scriptCleaner)
        registerHandler(XmlTokenType.XML_EMPTY_ELEMENT_END, scriptCleaner)
    }

    override fun getTokenType(): IElementType? {
        val type = super.getTokenType()
        if (type == XmlTokenType.TAG_WHITE_SPACE && baseState() == 0) return XmlTokenType.XML_REAL_WHITE_SPACE
        if (seenSeafoodAttribute && type == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN) return SeafoodElementTypes.EMBEDDED_TS
        return type
    }

    override fun findScriptContentProvider(mimeType: String?): HtmlScriptContentProvider? =
            findScriptContentProviderSeafood()

    override fun getStyleLanguage(): Language? {
        return styleViaLang(ourDefaultStyleLanguage) ?: super.getStyleLanguage()
    }

    override fun seenScript(): Boolean = seenScript
    override fun seenStyle(): Boolean = seenStyle
    override fun seenTemplate(): Boolean = seenTemplate
    override fun seenTag(): Boolean = seenTag
    override fun seenAttribute(): Boolean = seenAttribute
    override fun seenSeafoodAttribute(): Boolean = seenSeafoodAttribute
    override fun getScriptType(): String? = scriptType
    override fun getStyleType(): String? = styleType
    override fun inTagState(): Boolean = baseState() == _HtmlLexer.START_TAG_NAME

    override fun setSeenScriptType() {
        seenContentType = true
    }

    override fun setSeenScript() {
        seenScript = true
    }

    override fun setSeenStyleType() {
        seenStylesheetType = true
    }

    override fun setSeenTemplate(template:Boolean) {
        seenTemplate = template
    }

    override fun setSeenTag(tag: Boolean) {
        seenTag = tag
    }

    override fun setSeenAttribute(attribute: Boolean) {
        seenAttribute = attribute
    }

    override fun setSeenSeafoodAttribute(value: Boolean) {
        seenSeafoodAttribute = value
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        seenTemplate = initialState and SeafoodTemplateTagHandler.SEEN_TEMPLATE != 0
        seenSeafoodAttribute = initialState and SeafoodLexer.SEEN_SEAFOOD_ATTRIBUTE != 0
        super.start(buffer, startOffset, endOffset, initialState)
    }

    override fun getState(): Int {
        val state = super.getState()
        return state or
                (if (seenTemplate) SeafoodTemplateTagHandler.SEEN_TEMPLATE
                else if (seenSeafoodAttribute) SeafoodLexer.SEEN_SEAFOOD_ATTRIBUTE
                else 0)
    }

    override fun endOfTheEmbeddment(name:String?):Boolean {
        return super.endOfTheEmbeddment(name) ||
                seenTemplate && "template" == name
    }

    private fun baseState() = state and BASE_STATE_MASK
}