package fistache.component.dependencies

import com.intellij.lang.typescript.modules.TypeScriptNodeReference
import com.intellij.lang.typescript.tsconfig.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.lang.typescript.tsconfig.TypeScriptImportsResolverProvider
import fistache.component.index.hasFistache

const val fistacheExtension = ".ft"
val defaultExtensionsWithDot = arrayOf(fistacheExtension)

class FistacheTypeScriptImportsResolverProvider : TypeScriptImportsResolverProvider {
    override fun useExplicitExtension(extensionWithDot: String): Boolean = extensionWithDot == fistacheExtension
    override fun getExtensions(): Array<String> = defaultExtensionsWithDot

    override fun createResolver(project: Project,
                                context: TypeScriptImportResolveContext,
                                contextFile: VirtualFile): TypeScriptFileImportsResolver? {
        if (!hasFistache(project)) return null

        val defaultProvider = TypeScriptImportsResolverProvider.getDefaultProvider(project, context, contextFile)
        val fistacheFileImportsResolver = FistacheFileImportsResolver(project, context, typeScriptNodeResolver(project))
        return flattenAndAppendResolver(defaultProvider, fistacheFileImportsResolver)
    }

    override fun createResolver(project: Project, config: TypeScriptConfig): TypeScriptFileImportsResolver? {
        if (!hasFistache(project)) return null

        val defaultProvider = TypeScriptImportsResolverProvider.getDefaultProvider(project, config)
        val nodeProcessor = typeScriptNodeResolver(project)
        val fistacheFileImportsResolver = FistacheFileImportsResolver(project, config.resolveContext, nodeProcessor)
        return flattenAndAppendResolver(defaultProvider, fistacheFileImportsResolver)
    }

    private fun flattenAndAppendResolver(defaultProvider: TypeScriptFileImportsResolver,
                                         fistacheFileImportsResolver: FistacheFileImportsResolver): TypeScriptCompositeImportsResolverImpl {
        val result = mutableListOf<TypeScriptFileImportsResolver>()
        if (defaultProvider is TypeScriptCompositeImportsResolverImpl) {
            result.addAll(defaultProvider.resolvers)
        }
        else {
            result.add(defaultProvider)
        }


        result.add(fistacheFileImportsResolver)

        return TypeScriptCompositeImportsResolverImpl(result)
    }

    private fun typeScriptNodeResolver(project: Project) =
            TypeScriptNodeReference.TypeScriptNodeModuleDirectorySearchProcessor(project)
}
