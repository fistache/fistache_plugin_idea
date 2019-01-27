package seafood.component.index

import com.intellij.javascript.nodejs.PackageJsonData
import com.intellij.lang.javascript.buildTools.npm.PackageJsonUtil
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootModificationTracker
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.search.GlobalSearchScope
import seafood.component.SeafoodFileType

const val SEAFOOD: String = "seafood"

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