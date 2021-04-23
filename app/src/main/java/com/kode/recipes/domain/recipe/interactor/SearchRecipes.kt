package com.kode.recipes.domain.recipe.interactor

import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import com.kode.recipes.domain.base.interactor.UseCase
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.SearchBy
import com.kode.recipes.domain.recipe.entity.SearchQuery
import com.kode.recipes.domain.recipe.exception.RecipesMissingFailure
import java.util.*
import javax.inject.Inject

class SearchRecipes @Inject constructor() : UseCase<List<Recipe>, SearchQuery>() {
    override suspend fun run(params: SearchQuery): Either<Failure, List<Recipe>> {
        val (constraint, searchBy, unfilteredRecipes) = params
        // Возвращаем ошибку, если список неотсортированных рецептов пуст
        if (unfilteredRecipes.isEmpty()) return Either.Left(RecipesMissingFailure)

        return if (constraint.isBlank()) {
            // Не осуществляем поиск, если запрос пуст
            Either.Right(unfilteredRecipes)
        } else {
            // Запрос
            val filterPattern = constraint.toLowerCase(Locale.getDefault()).trim()
            Either.Right(searchByField(filterPattern, searchBy, unfilteredRecipes))
        }
    }

    private fun searchByField(
        filterPattern: String,
        searchBy: SearchBy,
        unfilteredRecipes: List<Recipe>
    ): List<Recipe> {
        return when (searchBy) {
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

    // Возвращает boolean, означающий наличие в данной строке
    // указанного filterPattern, не учитывая регистр.
    // Если строка == null, возвращается false (проверка == true в конце)
    private fun String?.lowerCaseContains(filterPattern: String) =
        this?.toLowerCase(Locale.getDefault())?.contains(filterPattern) == true
}