package com.kode.recipes.data.recipe.repository

import com.kode.recipes.data.base.network.SafeApiCall
import com.kode.recipes.data.recipe.converter.toRecipe
import com.kode.recipes.data.recipe.network.RecipeApi
import com.kode.recipes.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl
@Inject constructor(
    private val recipeApi: RecipeApi,
    private val safeApiCall: SafeApiCall
) : RecipeRepository {

    override suspend fun getRecipes() = safeApiCall.safeApiResult({
        recipeApi.getRecipesAsync()
    }, { it.recipes.map { recipeDto -> recipeDto.toRecipe() } })

    override suspend fun getRecipeDetails(uuid: String) = safeApiCall.safeApiResult({
        recipeApi.getRecipeDetailsAsync(uuid)
    }, { it.recipe.toRecipe() })
}