package seafood.component.index

import com.intellij.lang.javascript.psi.JSImplicitElementProvider
import com.intellij.lang.javascript.psi.stubs.impl.JSImplicitElementImpl
import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey

class SeafoodIndexBase(private val key: StubIndexKey<String, JSImplicitElementProvider>,
                       jsKey: String) : StringStubIndexExtension<JSImplicitElementProvider>() {
    private val VERSION = 25

    init {
        // this is called on index==application component initialization
        JSImplicitElementImpl.ourUserStringsRegistry.registerUserString(jsKey)
    }

    companion object {
        fun createJSKey(key: StubIndexKey<String, JSImplicitElementProvider>): String =
                key.name.split(".").joinToString("") { it.subSequence(0, 1) }
    }

    override fun getKey(): StubIndexKey<String, JSImplicitElementProvider> = key

    override fun getVersion(): Int {
        return VERSION
    }
}