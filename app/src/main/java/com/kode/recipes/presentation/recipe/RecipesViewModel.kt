package com.kode.recipes.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kode.recipes.R
import com.kode.recipes.domain.base.interactor.None
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.interactor.GetRecipes
import com.kode.recipes.presentation.base.BaseViewModel
import com.kode.recipes.presentation.base.ItemClickedInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel
@Inject constructor(private val getRecipes: GetRecipes) : BaseViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _selectedRecipe = MutableLiveData<Recipe>()
    val selectedRecipe: LiveData<Recipe> = _selectedRecipe

    // При нажатии на рецепт сохраняем выбранный рецепт
    // и переходим на фрагмент деталей
    val onItemClicked = ItemClickedInterface<Recipe> {
        _selectedRecipe.value = it
        setNewDestination(R.id.action_recipesListFragment_to_recipeDetailsFragment)
    }

    init {
        getRecipes(
            params = None(),
            job = job,
            onResult = { it.fold(::handleFailure, ::handleRecipesLoaded) }
        )
    }

    private fun handleRecipesLoaded(recipes: List<Recipe>) {
        _recipes.value = recipes
    }

}