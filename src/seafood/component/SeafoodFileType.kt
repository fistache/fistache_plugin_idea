package seafood.component

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class SeafoodFileType : LanguageFileType(SeafoodLanguage.INSTANCE) {
    companion object {
        val INSTANCE: SeafoodFileType = SeafoodFileType()
        const val DEFAULT_EXTENSION = "seafood"
    }

    override fun getName(): String {
        return "Seafood component"
    }

    override fun getDescription(): String {
        return "Seafood component"
    }

    override fun getDefaultExtension(): String {
        return DEFAULT_EXTENSION
    }

    override fun getIcon(): Icon? {
        return IconPack.SEAFOOD_COMPONENT
    }
}
