package seafood.component.index

import com.intellij.lang.javascript.psi.JSImplicitElementProvider
import com.intellij.psi.stubs.StubIndexKey

class SeafoodOptionsIndex : SeafoodIndexBase(KEY, JS_KEY) {
    companion object {
        val KEY: StubIndexKey<String, JSImplicitElementProvider> = StubIndexKey.createIndexKey<String, JSImplicitElementProvider>("seafood.options.index")
        val JS_KEY: String = createJSKey(KEY)
    }
}