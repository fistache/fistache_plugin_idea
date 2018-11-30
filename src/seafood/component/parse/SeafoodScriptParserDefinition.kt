package seafood.component.parse

import com.intellij.lang.PsiParser
import com.intellij.lang.javascript.JSFlexAdapter
import com.intellij.lang.javascript.dialects.TypeScriptLanguageDialect
import com.intellij.lang.javascript.dialects.TypeScriptParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IFileElementType
import seafood.component.language.SeafoodElementTypes

class SeafoodScriptParserDefinition : TypeScriptParserDefinition() {
    override fun createParser(project: Project?): PsiParser {
        return PsiParser { root, builder ->
            SeafoodScriptParser(builder).parseSeafood(root)
            return@PsiParser builder.treeBuilt
        }
    }

    override fun createLexer(project: Project?): Lexer {
        return JSFlexAdapter(TypeScriptLanguageDialect.DIALECT_OPTION_HOLDER)
    }

    override fun getFileNodeType(): IFileElementType {
        return SeafoodElementTypes.FILE
    }
}