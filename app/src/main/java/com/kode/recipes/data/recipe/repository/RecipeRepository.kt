package com.kode.recipes.data.recipe.repository

import com.kode.recipes.data.base.ApiFactory
import com.kode.recipes.data.base.BaseRepository
import com.kode.recipes.data.recipe.converter.toRecipe
import com.kode.recipes.data.recipe.network.RecipeApi
import com.kode.recipes.domain.recipe.Recipe

class RecipeRepository : BaseRepository() {

    // Ретрофит имплементирует интерфейс RecipeApi с запросами
    private val recipeApi: RecipeApi = ApiFactory.retrofit.create(RecipeApi::class.java)

    suspend fun getRecipes(): List<Recipe>? {
        return safeApiCall(
            call = { recipeApi.getRecipesAsync() },
            errorMessage = "Error while trying to get recipes"
        )?.recipes?.map { it.toRecipe() }
    }
}