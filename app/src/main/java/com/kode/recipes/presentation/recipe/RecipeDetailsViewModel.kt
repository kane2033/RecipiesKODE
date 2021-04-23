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
    private val requestRecipesDetails: GetRecipeDetails,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    // Айди текущего отображаемого рецепта
    private val currentRecipeId = savedStateHandle.get<String>(RecipeDetailsConstants.UUID_KEY)

    // Айди выбранного рецепта через "Рекомендованные"
    private val _nextSelectedRecipeId = MutableLiveData<Event<String>>()
    val selectedRecipeUuid: LiveData<Event<String>> = _nextSelectedRecipeId

    private val _recipeDetails = MutableLiveData<Recipe>()
    val recipeDetails: LiveData<Recipe> = _recipeDetails

    val onRecommendedItemClicked = ItemClickedInterface<RecipeBrief> {
        _nextSelectedRecipeId.value = Event(it.uuid)
    }

    fun requestRecipeDetails(uuid: String? = currentRecipeId) {
        if (uuid == null) return

        _isLoading.value = true
        requestRecipesDetails.invoke(
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
        // При старте экрана деталей, загружаем детали рецепта по переданному из списка uuid
        requestRecipeDetails()
    }

}