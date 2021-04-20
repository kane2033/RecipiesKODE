package com.kode.recipes.domain.recipe.entity

data class SearchQuery(
    val constraint: String,
    val searchBy: SearchBy,
    val unfilteredRecipes: List<Recipe>
) {
}