package com.kode.recipes.data.recipe.network

import com.kode.recipes.data.recipe.model.RecipeDto
import com.kode.recipes.data.recipe.model.RecipesListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {

    @GET("recipes")
    suspend fun getRecipesAsync(): Response<RecipesListDto>

    @GET("recipes/{uuid}")
    suspend fun getRecipeDetailsAsync(@Path("uuid") uuid: String): Response<RecipeDto>
}