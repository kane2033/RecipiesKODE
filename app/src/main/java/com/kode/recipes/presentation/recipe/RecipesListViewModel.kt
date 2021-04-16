package com.kode.recipes.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kode.recipes.domain.base.functional.Event
import com.kode.recipes.domain.base.interactor.None
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.SearchBy
import com.kode.recipes.domain.recipe.entity.SortBy
import com.kode.recipes.domain.recipe.interactor.GetRecipes
import com.kode.recipes.presentation.base.BaseViewModel
import com.kode.recipes.presentation.base.ItemClickedInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel
@Inject constructor(private val getRecipes: GetRecipes) : BaseViewModel() {

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
        unfilteredRecipes = recipes
    }

    fun searchRecipes(constraint: String) {
        viewModelScope.launch {
            val filteredList = mutableListOf<Recipe>()
            val defaultLocale = Locale.getDefault()
            withContext(Dispatchers.IO + job) {
                if (constraint.isBlank()) {
                    // Не осуществляем поиск, если запрос пуст
                    filteredList.addAll(unfilteredRecipes)
                } else {
                    // Запрос
                    val filterPattern = constraint.toLowerCase(defaultLocale).trim()
                    // Поле, по которому будем искать
                    searchByField(filterPattern, filteredList, defaultLocale)
                }
            }
            _recipes.value = filteredList
        }
    }

    private fun searchByField(
        filterPattern: String,
        filteredList: MutableList<Recipe>,
        defaultLocale: Locale
    ) {
        when (searchBy) {
            SearchBy.NAME -> {
                unfilteredRecipes.forEach {
                    if (it.name.toLowerCase(defaultLocale).contains(filterPattern)) {
                        filteredList.add(it)
                    }
                }
            }
            SearchBy.DESCRIPTION -> {
                unfilteredRecipes.forEach {
                    if (it.description?.toLowerCase(defaultLocale)
                            ?.contains(filterPattern) == true
                    ) {
                        filteredList.add(it)
                    }
                }
            }
            SearchBy.INSTRUCTIONS -> {
                unfilteredRecipes.forEach {
                    if (it.instructions?.toLowerCase(defaultLocale)
                            ?.contains(filterPattern) == true
                    ) {
                        filteredList.add(it)
                    }
                }
            }
        }
    }

    fun sortRecipes(sortBy: SortBy) {
        // Избегаем повторной сортировки
        if (sortedBy != null && sortBy == sortedBy) return

        _isLoading.value = true
        sortedBy = sortBy
        viewModelScope.launch {
            withContext(Dispatchers.IO + job) {
                when (sortBy) {
                    SortBy.NAME -> _recipes.value?.sortedBy { it.name }
                    SortBy.LAST_UPDATED -> _recipes.value?.sortedByDescending { it.lastUpdated }
                }
            }?.let { _recipes.value = it }
            _isLoading.value = false
        }
    }
}