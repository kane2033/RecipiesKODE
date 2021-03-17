package com.kode.recipes.presentation.recipe

import androidx.lifecycle.MutableLiveData
import com.kode.recipes.data.recipe.repository.RecipeRepository
import com.kode.recipes.domain.recipe.Recipe
import com.kode.recipes.presentation.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipesViewModel : BaseViewModel() {

    private val repository = RecipeRepository()

    val recipes = MutableLiveData<List<Recipe>>()

    val selectedRecipe = MutableLiveData<Recipe>()

    init {
        getRecipes()
    }

    fun getRecipes() {
        CoroutineScope(job + Dispatchers.Default).launch {
            val recipesList = repository.getRecipes()
            withContext(Dispatchers.Main) {
                recipes.value = recipesList ?: emptyList()
            }
        }
    }

    fun onRecipeClicked() {

    }

}