package com.kode.recipes.domain.recipe.interactor

import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import com.kode.recipes.domain.base.functional.flatMap
import com.kode.recipes.domain.base.interactor.UseCase
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.SearchBy
import com.kode.recipes.domain.recipe.entity.SearchQuery
import com.kode.recipes.domain.recipe.repository.RecipeRepository
import java.util.*
import javax.inject.Inject

class SearchRecipes
@Inject constructor(private val recipeRepository: RecipeRepository) :
    UseCase<List<Recipe>, SearchQuery>() {

    override suspend fun run(params: SearchQuery): Either<Failure, List<Recipe>> {
        // Цепочка вызовов - сначала берем из репозитория рецепты,
        // после фильтруем их
        return recipeRepository.getRecipesCached().flatMap {
            return@flatMap Either.Right(doSearch(params, it))
        }
    }

    private fun doSearch(searchQuery: SearchQuery, unfilteredRecipes: List<Recipe>): List<Recipe> {
        val (constraint, searchBy) = searchQuery
        return if (constraint.isBlank()) {
            // Не осуществляем поиск, если запрос пуст
            unfilteredRecipes
        } else {
            // Осуществляем фильтрацию (поиск)
            val filterPattern = constraint.toLowerCase(Locale.getDefault()).trim()
            when (searchBy) {
                SearchBy.NAME -> {
                    unfilteredRecipes.filter { it.name.lowerCaseContains(filterPattern) }
                }
                SearchBy.DESCRIPTION -> {
                    unfilteredRecipes.filter { it.description.lowerCaseContains(filterPattern) }
                }
                SearchBy.INSTRUCTIONS -> {
                    unfilteredRecipes.filter { it.instructions.lowerCaseContains(filterPattern) }
                }
            }
        }
    }

    // Возвращает boolean, означающий наличие в данной строке
    // указанного filterPattern, не учитывая регистр.
    // Если строка == null, возвращается false (проверка == true в конце)
    private fun String?.lowerCaseContains(filterPattern: String) =
        this?.toLowerCase(Locale.getDefault())?.contains(filterPattern) == true
}