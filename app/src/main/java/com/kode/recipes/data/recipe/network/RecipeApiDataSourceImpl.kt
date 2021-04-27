package com.kode.recipes.data.recipe.network

import com.kode.recipes.data.base.network.SafeApiCall
import com.kode.recipes.data.recipe.network.converter.toRecipe
import retrofit2.Retrofit
import javax.inject.Inject

class RecipeApiDataSourceImpl
@Inject constructor(retrofit: Retrofit, private val safeApiCall: SafeApiCall) :
    RecipeApiDataSource {

    private val api by lazy { retrofit.create(RecipeApi::class.java) }

    override suspend fun getRecipes() = safeApiCall.safeApiResult(
        call = api::getRecipesAsync,
        transform = { it.recipes.map { recipeDto -> recipeDto.toRecipe() } }
    )

    override suspend fun getRecipeDetails(uuid: String) = safeApiCall.safeApiResult(
        call = { api.getRecipeDetailsAsync(uuid) },
        transform = { it.recipe.toRecipe() }
    )
}