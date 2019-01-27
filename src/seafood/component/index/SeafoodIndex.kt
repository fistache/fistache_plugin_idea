package seafood.component.index

import com.intellij.javascript.nodejs.PackageJsonData
import com.intellij.lang.javascript.buildTools.npm.PackageJsonUtil
import com.intellij.lang.javascript.psi.JSImplicitElementProvider
import com.intellij.lang.javascript.psi.stubs.JSImplicitElement
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import com.intellij.util.Processor
import seafood.component.SeafoodFileType
import seafood.component.codeInsight.fromAsset
import seafood.component.index.SeafoodIndexBase.Companion.createJSKey

const val SEAFOOD: String = "seafood"
const val GLOBAL_BINDING_MARK: String = "*"

fun resolve(name:String, scope:GlobalSearchScope, key:StubIndexKey<String, JSImplicitElementProvider>): Collection<JSImplicitElement>? {
    if (DumbService.isDumb(scope.project!!)) return null
    val normalized = normalizeNameForIndex(name)
    val indexKey = createJSKey(key)

    val result = mutableListOf<JSImplicitElement>()
    StubIndex.getInstance().processElements(key, normalized, scope.project!!, scope, JSImplicitElementProvider::class.java, Processor {
        provider: JSImplicitElementProvider? ->
        provider?.indexingData?.implicitElements
                // the check for name is needed for groups of elements, for instance:
                // directives: {a:..., b:...} -> a and b are recorded in 'directives' data.
                // You can find it with 'a' or 'b' key, but you should filter the result
                ?.filter { it.userString == indexKey && normalized == it.name }
                ?.forEach { result.add(it) }
        return@Processor true
    })
    return if (result.isEmpty()) null else result
}

fun hasSeafood(project: Project): Boolean {
    if (DumbService.isDumb(project)) return false

    return CachedValuesManager.getManager(project).getCachedValue(project) {
        var hasSeafood = false
        var packageJson: VirtualFile? = null
        if (project.baseDir != null) {
            packageJson = project.baseDir.findChild(PackageJsonUtil.FILE_NAME)
            if (packageJson != null) {
                val packageJsonData = PackageJsonData.getOrCreate(packageJson)
                if (packageJsonData.isDependencyOfAnyType(SEAFOOD)) {
                    hasSeafood = true
                }
            }
        }

        if (hasSeafood) {
            CachedValueProvider.Result.create(true, packageJson)
        }
        else {
            val result = FileTypeIndex.containsFileOfType(SeafoodFileType.INSTANCE, GlobalSearchScope.projectScope(project))
            CachedValueProvider.Result.create(result, VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS,
                    ProjectRootModificationTracker.getInstance(project))
        }
    }
}

private fun normalizeNameForIndex(name: String) = fromAsset(name.substringBeforeLast(GLOBAL_BINDING_MARK))

class SeafoodIndexData(val originalName: String,
                   val nameRef: String?, val descriptorRef: String?, val groupRegistration: Boolean, val isGlobal: Boolean) {
    fun isGlobalExact(): Boolean = isGlobal && !originalName.endsWith(GLOBAL_BINDING_MARK)
}
