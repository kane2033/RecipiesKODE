package com.kode.recipes.data.base

/**
 * Класс используется для обработки ответа сети:
 * успешно или ошибка.
 * */
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}