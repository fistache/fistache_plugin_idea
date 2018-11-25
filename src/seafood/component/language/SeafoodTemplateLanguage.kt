package seafood.component.language

import com.intellij.lang.PsiBuilder
import com.intellij.lang.javascript.*
import com.intellij.lang.javascript.parsing.JavaScriptParser
import seafood.component.SeafoodFileType

class SeafoodTemplateLanguage : JSLanguageDialect("SeafoodTemplate", DialectOptionHolder.ECMA_6, JavaScriptSupportLoader.ECMA_SCRIPT_6) {
    override fun getFileExtension(): String {
        return SeafoodFileType.DEFAULT_EXTENSION
    }

    override fun createParser(builder: PsiBuilder): JavaScriptParser<*, *, *, *> {
        return SeafoodTemplateParser(builder)
    }

    companion object {
        val INSTANCE: SeafoodTemplateLanguage = SeafoodTemplateLanguage()
    }
}