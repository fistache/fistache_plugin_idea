package seafood.component

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory
import org.jetbrains.jps.model.fileTypes.FileNameMatcherFactory

class SeafoodFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(consumer: FileTypeConsumer) {
        consumer.consume(SeafoodFileType.INSTANCE, FileNameMatcherFactory.getInstance().createMatcher("*." + SeafoodFileType.INSTANCE.defaultExtension))
    }
}
