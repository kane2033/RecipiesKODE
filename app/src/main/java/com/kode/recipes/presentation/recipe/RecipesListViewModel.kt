package com.kode.recipes.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kode.recipes.domain.base.functional.Event
import com.kode.recipes.domain.base.interactor.None
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.SortBy
import com.kode.recipes.domain.recipe.interactor.GetRecipes
import com.kode.recipes.presentation.base.BaseViewModel
import com.kode.recipes.presentation.base.ItemClickedInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel
@Inject constructor(private val getRecipes: GetRecipes) : BaseViewModel() {

    // Список рецептов
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    // По полю айди осуществляется навигация на фрагмент с деталями рецепта
    private val _selectedRecipeUuid = MutableLiveData<Event<String>>()
    val selectedRecipeUuid: LiveData<Event<String>> = _selectedRecipeUuid

    // При нажатии на рецепт сохраняется айди выбранного рецепта
    val onItemClicked = ItemClickedInterface<Recipe> {
        _selectedRecipeUuid.value = Event(it.uuid)
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