package com.kode.recipes.domain.recipe.repository

import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import com.kode.recipes.domain.recipe.entity.Recipe

interface RecipeRepository {
    suspend fun getRecipes(): Either<Failure, List<Recipe>>
    suspend fun getRecipesCached(): Either<Failure, List<Recipe>>
    suspend fun getRecipeDetails(uuid: String): Either<Failure, Recipe>
}