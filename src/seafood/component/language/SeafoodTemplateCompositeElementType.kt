package seafood.component.language

import com.intellij.psi.tree.ICompositeElementType
import com.intellij.psi.tree.IElementType

abstract class SeafoodTemplateCompositeElementType(debugName: String) : IElementType(debugName, SeafoodTemplateLanguage.INSTANCE), ICompositeElementType
