package com.kode.recipes.data.recipe.model

data class RecipeDto(
    val uuid: String,
    val name: String,
    val images: List<String>,
    val lastUpdated: String,
    val description: String?,
    val instructions: String?,
    val difficulty: Int,
    val similar: List<RecipeBriefDto>?
) {
}