package fistache.component

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory
import org.jetbrains.jps.model.fileTypes.FileNameMatcherFactory

class FistacheFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(consumer: FileTypeConsumer) {
        consumer.consume(FistacheFileType.INSTANCE, FileNameMatcherFactory.getInstance().createMatcher("*." + FistacheFileType.INSTANCE.defaultExtension))
    }
}
