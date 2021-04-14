package com.kode.recipes.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kode.recipes.R
import com.kode.recipes.domain.base.interactor.None
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.SortBy
import com.kode.recipes.domain.recipe.interactor.GetRecipeDetails
import com.kode.recipes.domain.recipe.interactor.GetRecipes
import com.kode.recipes.presentation.base.BaseViewModel
import com.kode.recipes.presentation.base.ItemClickedInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel
@Inject constructor(
    private val getRecipes: GetRecipes,
    private val getRecipeDetails: GetRecipeDetails
) : BaseViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _recipeDetails = MutableLiveData<Recipe>()
    val recipeDetails: LiveData<Recipe> = _recipeDetails

    // При нажатии на рецепт сохраняем выбранный рецепт
    // и переходим на фрагмент деталей
    val onItemClicked = ItemClickedInterface<Recipe> {
        setNewDestination(R.id.action_recipesListFragment_to_recipeDetailsFragment)
        getRecipeDetails(it.uuid)
    }

    private fun getRecipeDetails(uuid: String) {
        _isLoading.value = true
        getRecipeDetails.invoke(
            params = uuid,
            job = job,
            onResult = { result -> result.fold(::handleFailure, ::handleRecipeDetailsLoaded) }
        )
    }

    private fun handleRecipeDetailsLoaded(recipe: Recipe) {
        _isLoading.value = false
        _recipeDetails.value = recipe
    }

    init {
        getRecipes()
    }

    private fun getRecipes() {
        _isLoading.value = true
        getRecipes(
            params = None(),
            job = job,
            onResult = { it.fold(::handleFailure, ::handleRecipesLoaded) }
        )
    }

    private fun handleRecipesLoaded(recipes: List<Recipe>) {
        _isLoading.value = false
        _recipes.value = recipes
    }

    fun sortRecipes(sortBy: SortBy) {
        _isLoading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO + job) {
                when (sortBy) {
                    SortBy.NAME -> _recipes.value?.sortedBy { it.name }
                    SortBy.LAST_UPDATED -> _recipes.value?.sortedByDescending { it.lastUpdated }
                }
            }?.let { _recipes.value = it }
        }
        _isLoading.value = false
    }

}