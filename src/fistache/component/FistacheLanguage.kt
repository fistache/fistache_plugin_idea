package fistache.component

import com.intellij.lang.html.HTMLLanguage
import com.intellij.lang.xml.XMLLanguage

class FistacheLanguage: XMLLanguage(HTMLLanguage.INSTANCE, "Fistache") {
    companion object {
        val INSTANCE: FistacheLanguage = FistacheLanguage()
    }
}
