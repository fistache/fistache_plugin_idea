package seafood.component.syntax.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.xml.IXmlLeafElementType;

public interface SeafoodTokenType {
    IElementType XML_ATTRIBUTE_SINGLE_EXPRESSION_START = new IXmlLeafElementType("XML_ATTRIBUTE_SINGLE_EXPRESSION_START");
    IElementType XML_ATTRIBUTE_SINGLE_EXPRESSION_END = new IXmlLeafElementType("XML_ATTRIBUTE_SINGLE_EXPRESSION_END");
    IElementType XML_ATTRIBUTE_SINGLE_EXPRESSION_VALUE = new IXmlLeafElementType("XML_ATTRIBUTE_SINGLE_EXPRESSION_VALUE");
}
