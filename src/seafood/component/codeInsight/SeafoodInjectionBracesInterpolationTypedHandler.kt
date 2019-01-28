package seafood.component.codeInsight

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.ide.highlighter.HtmlFileType
import com.intellij.lang.javascript.JSInjectionBracesUtil
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Pair
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.xml.XmlElement
import com.intellij.psi.xml.XmlTag
import com.intellij.util.NullableFunction
import seafood.component.SeafoodFileType
import seafood.component.index.hasSeafood
import seafood.component.syntax.SeafoodInjector

private val myBracesCompleter: JSInjectionBracesUtil.InterpolationBracesCompleter =
        SeafoodInterpolationBracesCompleter(SeafoodInjector.BRACES_FACTORY)

class SeafoodInjectionBracesInterpolationTypedHandler : TypedHandlerDelegate() {
    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): TypedHandlerDelegate.Result {
        if (!hasSeafood(project) ||
                fileType != SeafoodFileType.INSTANCE && fileType != HtmlFileType.INSTANCE) return Result.CONTINUE
        return myBracesCompleter.beforeCharTyped(c, project, editor, file)
    }
}

private val myExcludedTopLevelTags = arrayOf("script", "style")
class SeafoodInterpolationBracesCompleter(factory: NullableFunction<PsiElement, Pair<String, String>>) :
        JSInjectionBracesUtil.InterpolationBracesCompleter(factory) {

    override fun checkTypingContext(editor: Editor, file: PsiFile): Boolean {
        val atCaret = getContextElement(editor, file)
        val tag = atCaret as? XmlTag ?: atCaret?.parent as? XmlTag
        return atCaret == null || atCaret is XmlElement && tag?.name !in myExcludedTopLevelTags
    }
}