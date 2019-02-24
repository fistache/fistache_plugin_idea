package fistache.component

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class FistacheFileType : LanguageFileType(FistacheLanguage.INSTANCE) {
    companion object {
        val INSTANCE: FistacheFileType = FistacheFileType()
        const val DEFAULT_EXTENSION = "fistache"
    }

    override fun getName(): String {
        return "Fistache component"
    }

    override fun getDescription(): String {
        return "Fistache component"
    }

    override fun getDefaultExtension(): String {
        return DEFAULT_EXTENSION
    }

    override fun getIcon(): Icon? {
        return fistache.component.IconPack.FISTACHE_COMPONENT
    }
}
