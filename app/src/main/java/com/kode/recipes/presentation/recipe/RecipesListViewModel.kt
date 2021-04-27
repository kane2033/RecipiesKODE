package com.kode.recipes.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kode.recipes.domain.base.functional.Event
import com.kode.recipes.domain.base.interactor.None
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.SearchBy
import com.kode.recipes.domain.recipe.entity.SearchQuery
import com.kode.recipes.domain.recipe.entity.SortBy
import com.kode.recipes.domain.recipe.interactor.GetRecipes
import com.kode.recipes.domain.recipe.interactor.SearchRecipes
import com.kode.recipes.domain.recipe.interactor.SortRecipes
import com.kode.recipes.presentation.base.BaseViewModel
import com.kode.recipes.presentation.base.ItemClickedInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel
@Inject constructor(
    private val requestRecipes: GetRecipes,
    private val searchRecipes: SearchRecipes,
    private val sortRecipes: SortRecipes
) : BaseViewModel() {

    // Список рецептов (отображаемый, может фильтроваться)
    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    // Неотсортированный список хранит изначальный список рецептов
    private var unfilteredRecipes = listOf<Recipe>()

    // По полю айди осуществляется навигация на фрагмент с деталями рецепта
    private val _selectedRecipeUuid = MutableLiveData<Event<String>>()
    val selectedRecipeUuid: LiveData<Event<String>> = _selectedRecipeUuid

    // При нажатии на рецепт сохраняется айди выбранного рецепта
    val onItemClicked = ItemClickedInterface<Recipe> {
        _selectedRecipeUuid.value = Event(it.uuid)
    }

    // Поле хранит как был отсортирован список,
    // чтобы не сортировать лишний раз
    // при вызове той же сортировки
    private var sortedBy: SortBy? = null

    // Тип поиска задается через интерфейс
    var searchBy: SearchBy = SearchBy.NAME

    init {
        requestRecipes()
    }

    // Получение списка рецептов
    // с сервиса
    fun requestRecipes() {
        _isLoading.value = true
        requestRecipes(
            params = None(),
            job = job,
            onResult = { it.fold(::handleFailure, ::handleRecipesLoaded) }
        )
    }

    private fun handleRecipesLoaded(recipes: List<Recipe>) {
        _isLoading.value = false
        _recipes.value = recipes
        unfilteredRecipes = recipes
    }

    // Поиск рецептов по запросу constraint
    fun searchRecipes(constraint: String) {
        _isLoading.value = true
        searchRecipes(
            params = SearchQuery(constraint, searchBy),
            job = job,
            onResult = { it.fold(::handleFailure, ::handleRecipesFiltered) }
        )
    }

    private fun handleRecipesFiltered(filteredRecipes: List<Recipe>) {
        _isLoading.value = false
        _recipes.value = filteredRecipes
    }

    // Сортировка рецептов
    fun sortRecipes(sortBy: SortBy) {
        // Избегаем повторной сортировки
        if (sortedBy != null && sortBy == sortedBy) return

        _isLoading.value = true
        sortedBy = sortBy
        sortRecipes(
            params = sortBy,
            job = job,
            onResult = { it.fold(::handleFailure, ::handleRecipesSorted) }
        )
    }

    private fun handleRecipesSorted(sortedRecipes: List<Recipe>) {
        _isLoading.value = false
        _recipes.value = sortedRecipes
    }
}