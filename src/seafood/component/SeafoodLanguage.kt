package seafood.component

import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.xml.XMLLanguage

class SeafoodLanguage: XMLLanguage(HTMLLanguage.INSTANCE, "Seafood") {
    companion object {
        val INSTANCE: SeafoodLanguage = SeafoodLanguage()
    }
}
