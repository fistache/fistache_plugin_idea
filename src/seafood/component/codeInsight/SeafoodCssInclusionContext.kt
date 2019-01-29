package seafood.component.codeInsight

import com.intellij.psi.PsiElement
import com.intellij.psi.css.resolve.CssInclusionContext
import seafood.component.SeafoodLanguage

class SeafoodCssInclusionContext : CssInclusionContext() {
    override fun processAllCssFilesOnResolving(context: PsiElement): Boolean {
        return context.containingFile?.language == SeafoodLanguage.INSTANCE
    }
}