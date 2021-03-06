package bk.personal.com.langdrop.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified VM : ViewModel> Fragment.provideViewModel(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> =
    OverridableLazy(viewModels(ownerProducer, factoryProducer))

class OverridableLazy<T>(var implementation: Lazy<T>): Lazy<T> {
    override val value
        get() = implementation.value
    override fun isInitialized() = implementation.isInitialized()
}
