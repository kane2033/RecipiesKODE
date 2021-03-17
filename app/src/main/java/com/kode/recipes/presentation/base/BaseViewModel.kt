package com.kode.recipes.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

/**
 * Базовый класс [ViewModel], хранящий элементы,
 * используемые в каждой viewmodel.
 * */
abstract class BaseViewModel : ViewModel() {

    // Работа, в скоупе которой будет выполняться
    // задача
    protected val job = Job()

    // Статус загрузки (используется с прогресс баром)
    val isLoading = MutableLiveData(false)

    // Отменяем работу, когда viewmodel
    // прекратила жизненный цикл
    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}