package com.kode.recipes.data.recipe.network

import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import com.kode.recipes.domain.recipe.entity.Recipe

interface RecipeApiDataSource {
    suspend fun getRecipes(): Either<Failure, List<Recipe>>
    suspend fun getRecipeDetails(uuid: String): Either<Failure, Recipe>
}