package fistache.component.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.css.resolve.CssInclusionContext
import fistache.component.FistacheLanguage

class FistacheCssInclusionContext : CssInclusionContext() {
    override fun processAllCssFilesOnResolving(context: PsiElement): Boolean {
        return context.containingFile?.language == FistacheLanguage.INSTANCE
    }
}