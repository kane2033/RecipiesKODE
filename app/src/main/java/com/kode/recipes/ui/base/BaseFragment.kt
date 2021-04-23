package com.kode.recipes.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import com.kode.recipes.R
import com.kode.recipes.domain.base.entity.FailureInfo
import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.presentation.base.BaseViewModel
import com.kode.recipes.presentation.base.FailureViewModel

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

    protected fun navigateTo(
        @IdRes action: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: Navigator.Extras? = null
    ) {
        findNavController().navigate(action, args, navOptions, extras)
    }

    protected fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(navDirections)
    }

    /**
     * Стандартный метод обработки ошибок, который дефолтно обрабатывает ошибку
     * отсутствия соединения [Failure.NetworkConnection] и, если не обработаны остальные ошибки,
     * отображает стандартное сообщение ошибки.
     * @param baseRetryClickedCallback - обязательный коллбэк, выполняемый при нажатии кнопки "повторить/обновить".
     * @param handleFailure - дополнительная обработка ошибок, присущая определенному экрану.
     * Стандартный параметр - отображение базового сообщения ошибки.
     * Для обработки доп. ошибок через данный параметр, необходимо создать функцию, в которой вернуть
     * объект [FailureInfo], содержащий сообщение ошибки и коллбек, производимый при получении
     * такой ошибки и нажатии кнопки "повторить/обновить".
     * @param handleRequestFailure - обработка HTTP ошибок по коду HTTP.
     * Стандартный параметр - никак не обрабатывается.
     * Для обработки доп. http ошибок необходимо создать функцию, возвращающую [FailureInfo]
     * в зависимости от переданного в параметры [Failure.RequestFailure.code]
     * */
    protected fun handleFailure(
        baseRetryClickedCallback: () -> Unit,
        handleFailure: (failure: Failure) -> FailureInfo? = { null },
        handleRequestFailure: (code: Int) -> FailureInfo? = { null }
    ) {
        // Наблюдаем за изменением переменной ошибки
        viewModel.failure.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { failure ->

                // Получаем FailureInfo - текст ошибки и коллбек при обновлении
                val failureInfo: FailureInfo? = when (failure) {
                    is Failure.NetworkConnection -> FailureInfo(
                        baseRetryClickedCallback,
                        getString(R.string.error_network_connection_title),
                        getString(R.string.error_network_connection)
                    )
                    is Failure.RequestFailure -> handleRequestFailure(failure.code)
                    else -> handleFailure(failure)
                }

                // Открываем диалог с текстом ошибки и кнопкой обновления
                if (failureInfo == null) {
                    // Если инфа об ошибке не указана (null), показываем базовую
                    openFailureDialog(getBaseFailureInfo(baseRetryClickedCallback))
                } else {
                    // Иначе показываем предоставленную инфу об ошибке
                    openFailureDialog(failureInfo)
                }
            }
        })
    }

    // Базовый текст ошибки, если параметр handleFailure не указан
    private fun getBaseFailureInfo(baseRetryClickedCallback: () -> Unit) = FailureInfo(
        baseRetryClickedCallback,
        getString(R.string.error_base_title),
        getString(R.string.error_base)
    )

    // Открытие диалогового фрагмента с ошибкой
    private fun openFailureDialog(failureInfo: FailureInfo) {
        // Открываем фрагмент, передавая инфу об ошибке
        navigateTo(
            R.id.failureFragment,
            bundleOf(FailureViewModel.FAILURE_INFO_KEY to failureInfo)
        )
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