package com.kode.recipes.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.kode.recipes.domain.base.functional.Event
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.RecipeBrief
import com.kode.recipes.domain.recipe.interactor.GetRecipeDetails
import com.kode.recipes.presentation.base.BaseViewModel
import com.kode.recipes.presentation.base.ItemClickedInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel
@Inject constructor(
    private val getRecipeDetails: GetRecipeDetails,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _selectedRecipeUuid = MutableLiveData<Event<String>>()
    val selectedRecipeUuid: LiveData<Event<String>> = _selectedRecipeUuid

    private val _recipeDetails = MutableLiveData<Recipe>()
    val recipeDetails: LiveData<Recipe> = _recipeDetails

    val onRecommendedItemClicked = ItemClickedInterface<RecipeBrief> {
        _selectedRecipeUuid.value = Event(it.uuid)
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
        // При старт
        savedStateHandle.get<String>("uuid")?.let { getRecipeDetails(it) }
    }

}