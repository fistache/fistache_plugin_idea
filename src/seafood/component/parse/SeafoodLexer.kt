package seafood.component.parse

import com.intellij.lang.HtmlScriptContentProvider
import com.intellij.lang.Language
import com.intellij.lexer.HtmlHighlightingLexer
import com.intellij.lexer.HtmlLexer
import com.intellij.lexer.Lexer
import com.intellij.lexer._HtmlLexer
import com.intellij.psi.tree.IElementType
import com.intellij.psi.xml.XmlTokenType
import org.jetbrains.vuejs.language.SeafoodLangAttributeHandler
import seafood.component.language.*

class SeafoodLexer : HtmlLexer(), SeafoodHandledLexer {
    companion object {
        const val SEEN_SEAFOOD_ATTRIBUTE: Int = 0x10000
    }

    private var interpolationLexer: Lexer? = null
    private var interpolationStart = -1
    private var seenTemplate: Boolean = false
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
    override fun inTagState(): Boolean = (state and HtmlHighlightingLexer.BASE_STATE_MASK) == _HtmlLexer.START_TAG_NAME

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
        seenSeafoodAttribute = initialState and SEEN_SEAFOOD_ATTRIBUTE != 0
        interpolationLexer = null
        interpolationStart = -1
        super.start(buffer, startOffset, endOffset, initialState)
    }

    override fun getState(): Int {
        val state = super.getState()
        return state or
                (if (seenTemplate) SeafoodTemplateTagHandler.SEEN_TEMPLATE
                else if (seenSeafoodAttribute) SEEN_SEAFOOD_ATTRIBUTE
                else 0)
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
        if (seenSeafoodAttribute && type == XmlTokenType.XML_ATTRIBUTE_VALUE_TOKEN) {
            return SeafoodElementTypes.EMBEDDED_TS
        }
        return type
    }
}