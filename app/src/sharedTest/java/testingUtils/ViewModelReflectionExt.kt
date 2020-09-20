package testingUtils

import androidx.lifecycle.ViewModel
import bk.personal.com.langdrop.utils.OverridableLazy
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.isAccessible

fun <VM : ViewModel, T> T.replace(
    viewModelDelegate: KProperty1<T, VM>, viewModel: VM
) {
    viewModelDelegate.isAccessible = true
    (viewModelDelegate.getDelegate(this) as
            OverridableLazy<VM>).implementation = lazy { viewModel }
}

