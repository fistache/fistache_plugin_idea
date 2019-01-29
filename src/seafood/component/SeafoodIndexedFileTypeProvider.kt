package seafood.component

import com.intellij.lang.javascript.index.IndexedFileTypeProvider
import com.intellij.openapi.fileTypes.FileType

class SeafoodIndexedFileTypeProvider : IndexedFileTypeProvider {
    override fun getFileTypesToIndex(): Array<FileType> = arrayOf(SeafoodFileType.INSTANCE)
}
