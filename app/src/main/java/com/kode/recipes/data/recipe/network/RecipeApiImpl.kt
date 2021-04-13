package com.kode.recipes.data.recipe.network

import retrofit2.Retrofit
import javax.inject.Inject

class RecipeApiImpl
@Inject constructor(retrofit: Retrofit) : RecipeApi {

    private val api by lazy { retrofit.create(RecipeApi::class.java) }

    override suspend fun getRecipesAsync() = api.getRecipesAsync()

    override suspend fun getRecipeDetailsAsync(uuid: String) = api.getRecipeDetailsAsync(uuid)
}