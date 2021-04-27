package com.kode.recipes.domain.recipe.interactor

import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import com.kode.recipes.domain.base.functional.flatMap
import com.kode.recipes.domain.base.interactor.UseCase
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.SortBy
import com.kode.recipes.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class SortRecipes
@Inject constructor(private val recipeRepository: RecipeRepository) :
    UseCase<List<Recipe>, SortBy>() {

    override suspend fun run(params: SortBy): Either<Failure, List<Recipe>> {
        // Цепочка вызовов - сначала берем из репозитория рецепты,
        // при успехе сортируем их
        return recipeRepository.getRecipesCached().flatMap {
            return@flatMap Either.Right(doSort(params, it))
        }
    }

    // Возвращаем отсортированный список рецептов
    // по заданному полю сортировки
    private fun doSort(
        sortBy: SortBy,
        unsortedRecipes: List<Recipe>
    ): List<Recipe> {
        return when (sortBy) {
            SortBy.NAME -> unsortedRecipes.sortedBy { it.name }
            SortBy.LAST_UPDATED -> unsortedRecipes.sortedByDescending { it.lastUpdated }
        }
    }
}
