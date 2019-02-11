package fistache.component.index

import com.intellij.lang.javascript.psi.JSImplicitElementProvider
import com.intellij.psi.stubs.StubIndexKey

class FistacheOptionsIndex : FistacheIndexBase(KEY, JS_KEY) {
    companion object {
        val KEY: StubIndexKey<String, JSImplicitElementProvider> = StubIndexKey.createIndexKey<String, JSImplicitElementProvider>("fistache.options.index")
        val JS_KEY: String = createJSKey(KEY)
    }
}