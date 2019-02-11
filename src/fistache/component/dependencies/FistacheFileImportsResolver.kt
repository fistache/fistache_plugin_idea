package fistache.component.dependencies

import com.intellij.javascript.nodejs.NodeModuleDirectorySearchProcessor
import com.intellij.lang.javascript.psi.JSExecutionScope
import com.intellij.lang.javascript.psi.stubs.TypeScriptScriptContentIndex
import com.intellij.lang.typescript.tsconfig.TypeScriptFileImportsResolverImpl
import com.intellij.lang.typescript.tsconfig.TypeScriptImportResolveContext
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.util.Processor
import fistache.component.FistacheFileType

class FistacheFileImportsResolver(project: Project,
                                  resolveContext: TypeScriptImportResolveContext,
                                  nodeProcessor: NodeModuleDirectorySearchProcessor) :
        TypeScriptFileImportsResolverImpl(project, resolveContext, nodeProcessor, defaultExtensionsWithDot, listOf(FistacheFileType.INSTANCE)) {

    override fun processAllFilesInScope(includeScope: GlobalSearchScope, processor: Processor<VirtualFile>) {
        StubIndex.getInstance().processElements(
                TypeScriptScriptContentIndex.KEY, TypeScriptScriptContentIndex.DEFAULT_INDEX_KEY, project,
                includeScope, null, JSExecutionScope::class.java) {

            ProgressManager.checkCanceled()
            val virtualFile = it.containingFile.virtualFile
            if (virtualFile != null) {
                if (!processor.process(virtualFile)) return@processElements false
            }
            return@processElements true
        }
    }
}