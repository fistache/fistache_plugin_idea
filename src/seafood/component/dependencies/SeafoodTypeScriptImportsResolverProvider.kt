package seafood.component.dependencies

import com.intellij.lang.typescript.modules.TypeScriptNodeReference
import com.intellij.lang.typescript.tsconfig.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.lang.typescript.tsconfig.TypeScriptImportsResolverProvider
import seafood.component.index.hasSeafood

const val seafoodExtension = ".seafood"
val defaultExtensionsWithDot = arrayOf(seafoodExtension)

class SeafoodTypeScriptImportsResolverProvider : TypeScriptImportsResolverProvider {
    override fun useExplicitExtension(extensionWithDot: String): Boolean = extensionWithDot == seafoodExtension
    override fun getExtensions(): Array<String> = defaultExtensionsWithDot

    override fun createResolver(project: Project,
                                context: TypeScriptImportResolveContext,
                                contextFile: VirtualFile): TypeScriptFileImportsResolver? {
        if (!hasSeafood(project)) return null

        val defaultProvider = TypeScriptImportsResolverProvider.getDefaultProvider(project, context, contextFile)
        val seafoodFileImportsResolver = SeafoodFileImportsResolver(project, context, typeScriptNodeResolver(project))
        return flattenAndAppendResolver(defaultProvider, seafoodFileImportsResolver)
    }

    override fun createResolver(project: Project, config: TypeScriptConfig): TypeScriptFileImportsResolver? {
        if (!hasSeafood(project)) return null

        val defaultProvider = TypeScriptImportsResolverProvider.getDefaultProvider(project, config)
        val nodeProcessor = typeScriptNodeResolver(project)
        val seafoodFileImportsResolver = SeafoodFileImportsResolver(project, config.resolveContext, nodeProcessor)
        return flattenAndAppendResolver(defaultProvider, seafoodFileImportsResolver)
    }

    private fun flattenAndAppendResolver(defaultProvider: TypeScriptFileImportsResolver,
                                         seafoodFileImportsResolver: SeafoodFileImportsResolver): TypeScriptCompositeImportsResolverImpl {
        val result = mutableListOf<TypeScriptFileImportsResolver>()
        if (defaultProvider is TypeScriptCompositeImportsResolverImpl) {
            result.addAll(defaultProvider.resolvers)
        }
        else {
            result.add(defaultProvider)
        }


        result.add(seafoodFileImportsResolver)

        return TypeScriptCompositeImportsResolverImpl(result)
    }

    private fun typeScriptNodeResolver(project: Project) =
            TypeScriptNodeReference.TypeScriptNodeModuleDirectorySearchProcessor(project)
}
