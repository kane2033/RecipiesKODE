package com.kode.recipes.domain.recipe

data class Recipe(
    val uuid: String,
    val name: String,
    val imageUrl: String,
    val description: String?,
    val instructions: String?,
    val difficulty: Int
) {
}