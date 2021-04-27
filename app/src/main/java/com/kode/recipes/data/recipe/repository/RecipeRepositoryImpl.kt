package com.kode.recipes.data.recipe.repository

import com.kode.recipes.data.base.network.SafeApiCall
import com.kode.recipes.data.recipe.converter.toRecipe
import com.kode.recipes.data.recipe.network.RecipeApi
import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import com.kode.recipes.domain.base.functional.onSuccess
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl
@Inject constructor(
    private val recipeApi: RecipeApi,
    private val safeApiCall: SafeApiCall
) : RecipeRepository {

    // Синглтон репозиторий хранит список рецептов,
    // чтобы не выполнять запрос на бэкенд повторно
    private var recipes: List<Recipe> = emptyList()

    override suspend fun getRecipes(): Either<Failure, List<Recipe>> {
        val result = safeApiCall.safeApiResult({
            recipeApi.getRecipesAsync()
        }, { it.recipes.map { recipeDto -> recipeDto.toRecipe() } })
        result.onSuccess { recipes = it }
        return result
    }

    // Возвращает сохраненный в синглтоне список рецептов
    override suspend fun getRecipesCached(): Either<Failure, List<Recipe>> {
        if (recipes.isEmpty()) return Either.Left(Failure.MissingContentFailure)
        return Either.Right(recipes)
    }

    override suspend fun getRecipeDetails(uuid: String) = safeApiCall.safeApiResult({
        recipeApi.getRecipeDetailsAsync(uuid)
    }, { it.recipe.toRecipe() })
}