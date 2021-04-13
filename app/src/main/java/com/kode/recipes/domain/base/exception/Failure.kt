package com.kode.recipes.domain.base.exception

/**
 * Базовый класс для обработки ошибок.
 * Каждая ошибка, связанная с api запросами, должна наследовать [RequestFailure]
 * Каждая ошибка, специфичная для для функции, должна наследовать [FeatureFailure].
 */
sealed class Failure(val exception: Exception = Exception("Failure")) {
    object None : Failure()
    object NetworkConnection : Failure()
    object MissingContentFailure : Failure()

    open class MultipleFailures(val failures: Collection<Failure> = emptyList()) : Failure()

    open class FeatureFailure(
        featureException: Exception = Exception("Feature failure")
    ) : Failure(featureException)

    open class RequestFailure(
        requestException: Exception = Exception("Request failure"),
        val code: Int, val message: String? = null
    ) : Failure(requestException) {

        override fun equals(other: Any?) = other is RequestFailure && other.code == this.code

        override fun hashCode() = code
    }


}