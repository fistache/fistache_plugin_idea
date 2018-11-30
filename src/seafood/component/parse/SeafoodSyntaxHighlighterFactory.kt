package seafood.component.parse

import com.intellij.ide.highlighter.HtmlFileHighlighter
import com.intellij.lexer.Lexer
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class SeafoodSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        if (project == null) return createHighlighter(SeafoodHighlightingLexer())
        return createHighlighter(SeafoodHighlightingLexer())
    }

    private fun createHighlighter(lexer: Lexer): SyntaxHighlighter {
        return object : HtmlFileHighlighter() {
            override fun getHighlightingLexer(): Lexer = lexer
        }
    }
}
