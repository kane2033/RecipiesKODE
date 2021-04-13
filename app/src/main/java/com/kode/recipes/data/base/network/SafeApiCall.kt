package com.kode.recipes.data.base.network

import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Безопасный вызов retrofit запроса с помощью [safeApiResult]
 * с возвратом тела запроса или [Failure].
 * @param [call] - сетевой запрос
 * */
@Singleton
class SafeApiCall
@Inject constructor(private val networkHandler: NetworkHandler) {

    // Получение результата запроса (парам. call)
    suspend fun <T, R> safeApiResult(
        call: suspend () -> Response<T>,
        transform: (T) -> R
    ): Either<Failure, R> {
        // Проверка на наличие доступа к сети и возвращение ошибки при отсутствии
        if (!networkHandler.isNetworkAvailable()) return Either.Left(Failure.NetworkConnection)

        // Получение результата запроса
        val response = call.invoke()

        // Возвращаем ошибку, если результат запроса [300; 500]
        if (!response.isSuccessful) return Either.Left(Failure.RequestFailure(code = response.code()))

        // Если тело результата запроса пусто, возвращаем ошибку
        val responseBody = response.body() ?: return Either.Left(Failure.MissingContentFailure)

        // Все проверки пройдены, возвращаем результат
        return Either.Right(transform(responseBody))
    }
}