package com.kode.recipes.presentation.base

class ItemClickListener<T>(private val clickListener: (item: T) -> Unit) {
    fun onClick(item: T) = clickListener.invoke(item)
}