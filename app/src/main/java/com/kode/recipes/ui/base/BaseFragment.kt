package com.kode.recipes.ui.base

import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.kode.recipes.presentation.base.BaseViewModel

/**
 * Базовый класс [Fragment],
 * имеющий общие для других фрагментов методы.
 * */
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {

    protected abstract val viewModel: BaseViewModel

    // Отображение Toast уведомления со строкой из ресурсов
    internal fun makeToast(@StringRes message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Отображение Toast уведомления с любой строкой
    internal fun makeToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    protected fun navigateTo(@IdRes action: Int) {
        findNavController().navigate(action)
    }

    protected fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    // Наблюдение за полем назначения навигации:
    // переходим на новый фрагмент только при первом изменении (event)
    protected fun observeNavigation() {
        viewModel.newDestination.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { destinationId ->
                navigateTo(destinationId)
            }
        })
    }
}