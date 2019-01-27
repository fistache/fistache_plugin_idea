package seafood.component.codeInsight

import com.intellij.lang.Language
import com.intellij.psi.css.EmbeddedCssProvider
import seafood.component.SeafoodLanguage

class SeafoodEmbeddedCssProvider : EmbeddedCssProvider() {
    override fun enableEmbeddedCssFor(language: Language): Boolean = language is SeafoodLanguage
}