package com.kode.recipes.presentation.base

fun interface ItemClickedInterface<T> {
    fun onClick(item: T)
}