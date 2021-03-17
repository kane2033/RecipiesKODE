package com.kode.recipes.data.recipe.network

import com.kode.recipes.data.recipe.model.RecipesListDto
import retrofit2.Response
import retrofit2.http.GET

interface RecipeApi {

    @GET("recipes")
    suspend fun getRecipesAsync(): Response<RecipesListDto>
}