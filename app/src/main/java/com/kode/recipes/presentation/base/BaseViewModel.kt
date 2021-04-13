package com.kode.recipes.presentation.base

import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Event
import kotlinx.coroutines.Job

/**
 * Базовый класс [ViewModel], хранящий элементы,
 * используемые в каждой viewmodel.
 * */
abstract class BaseViewModel : ViewModel() {

    // По данному полю определеяется пункт назначения навигации
    private val _newDestination = MutableLiveData<Event<Int>>()
    val newDestination: LiveData<Event<Int>> = _newDestination

    protected fun setNewDestination(@IdRes destinationId: Int) {
        _newDestination.value = Event(destinationId)
    }

    // Работа, в скоупе которой будет выполняться
    // задача
    protected val job = Job()

    // Статус загрузки (используется с прогресс баром)
    protected val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun setIsLoadingValue(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    // Упаковка ошибки в event, чтобы
    // отображение ошибки возникало только один раз
    protected val _failure = MutableLiveData<Event<Failure>>()
    val failure: LiveData<Event<Failure>> = _failure

    // Стандартный метод обработки ошибки, упаковывающий ее
    // в event
    protected fun handleFailure(failure: Failure) {
        _isLoading.value = false
        this._failure.value = Event(failure)
    }

    // Отменяем работу, когда viewmodel
    // прекратила жизненный цикл
    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}