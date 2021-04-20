package com.kode.recipes.domain.recipe.entity

data class SortQuery(
    val sortBy: SortBy,
    val unsortedRecipes: List<Recipe>?
) {
}