package com.kode.recipes.domain.base.functional

/**
 * Функциональный тип, представляющий собой одно из двух значений, но никогда не оба.
 * Экземпляры [Either] это либо экземпляр [Left], либо [Right].
 * В данном случае, [Left] всегда представляет собой ошибку (Failure), а
 * [Right] хранит успешный результат..
 *
 * @see Left
 * @see Right
 */
sealed class Either<out L, out R> {
    data class Left<out L>(val a: L) : Either<L, Nothing>()
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>

    val isLeft get() = this is Left<L>

    /**
     * Создает тип Left
     * @see Left
     */
    fun <L> left(a: L) = Either.Left(a)

    /**
     * Создает тип Right.
     * @see Right
     */
    fun <R> right(b: R) = Either.Right(b)

    fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
        f(this(it))
    }

    /** Выполнение соответствующей функции в зависимости от наличия соответствующего типа*/
    fun fold(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }
}

fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Left -> Either.Left(a)
        is Either.Right -> fn(b)
    }

/**
 * Возвращает значение правой части 'Right' или значение в левой части 'Left'.
 *  Right(12).getOrElse(17) RETURNS 12 and Left(12).getOrElse(17) RETURNS 17
 */
fun <L, R> Either<L, R>.getOrElse(value: R): R =
    when (this) {
        is Either.Left -> value
        is Either.Right -> b
    }

/**
 * Метод выполнит функциб @param [fn], если класс содержит левую часть 'Left'.
 * Возвращает Either, что позволяет создать цепь вызовов.
 */
fun <L, R> Either<L, R>.onFailure(fn: (failure: L) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Left) fn(a) }

/**
 * Метод выполнит функциб @param [fn], если класс содержит правую часть 'Right'.
 * Возвращает Either, что позволяет создать цепь вызовов.
 */
fun <L, R> Either<L, R>.onSuccess(fn: (success: R) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Right) fn(b) }


//fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::right))