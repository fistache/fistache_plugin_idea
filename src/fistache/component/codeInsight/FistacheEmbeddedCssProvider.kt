package fistache.component.codeInsight

import com.intellij.lang.Language
import com.intellij.psi.css.EmbeddedCssProvider
import fistache.component.FistacheLanguage

class FistacheEmbeddedCssProvider : EmbeddedCssProvider() {
    override fun enableEmbeddedCssFor(language: Language): Boolean = language is FistacheLanguage
}