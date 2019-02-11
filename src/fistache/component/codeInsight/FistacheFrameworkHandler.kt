package fistache.component.codeInsight

import com.intellij.lang.javascript.*
import com.intellij.lang.javascript.index.FrameworkIndexingHandler
import com.intellij.lang.javascript.psi.*
import com.intellij.psi.PsiElement
import com.intellij.psi.XmlElementVisitor
import com.intellij.psi.impl.source.PsiFileImpl
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlDocument
import com.intellij.psi.xml.XmlFile
import com.intellij.psi.xml.XmlTag
import com.intellij.xml.util.HtmlUtil
import fistache.component.FistacheFileType

class FistacheFrameworkHandler : FrameworkIndexingHandler()

fun findModule(element: PsiElement?): JSEmbeddedContent? {
    val file = element as? XmlFile ?: element?.containingFile as? XmlFile
    if (file != null && file.fileType == FistacheFileType.INSTANCE) {
        if (file is PsiFileImpl) {
            val greenStub = file.greenStub
            //stub-safe path
            if (greenStub != null) {
                val children = greenStub.getChildrenByType<JSElement>(JSExtendedLanguagesTokenSetProvider.MODULE_EMBEDDED_CONTENTS,
                        JSEmbeddedContent.ARRAY_FACTORY)
                val result = children.firstOrNull()
                return result as? JSEmbeddedContent
            }
        }
        val script = findScriptTag(file)
        if (script != null) {
            return PsiTreeUtil.findChildOfType(script, JSEmbeddedContent::class.java)
        }
    }
    return null
}

fun findScriptTag(xmlFile: XmlFile): XmlTag? {
    if (xmlFile.fileType == FistacheFileType.INSTANCE) {
        val visitor = MyScriptVisitor()
        xmlFile.accept(visitor)
        return visitor.scriptTag
    }
    return null
}

private class MyScriptVisitor : FistacheFileVisitor() {
    internal var jsElement: JSEmbeddedContent? = null
    internal var scriptTag: XmlTag? = null

    override fun visitXmlTag(tag: XmlTag?) {
        if (HtmlUtil.isScriptTag(tag)) {
            scriptTag = tag
            jsElement = PsiTreeUtil.findChildOfType(tag, JSEmbeddedContent::class.java)
        }
    }
}

open class FistacheFileVisitor : XmlElementVisitor() {
    override fun visitXmlDocument(document: XmlDocument?): Unit = recursion(document)

    override fun visitXmlFile(file: XmlFile?): Unit = recursion(file)

    protected fun recursion(element: PsiElement?) {
        element?.children?.forEach { it.accept(this) }
    }
}
