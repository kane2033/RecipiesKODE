package com.kode.recipes.domain.recipe.interactor

import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import com.kode.recipes.domain.base.interactor.UseCase
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.SortBy
import com.kode.recipes.domain.recipe.entity.SortQuery
import com.kode.recipes.domain.recipe.exception.RecipesMissingFailure
import javax.inject.Inject

class SortRecipes @Inject constructor() : UseCase<List<Recipe>, SortQuery>() {
    override suspend fun run(params: SortQuery): Either<Failure, List<Recipe>> {
        if (params.unsortedRecipes == null || params.unsortedRecipes.isEmpty())
            return Either.Left(RecipesMissingFailure)

        // Возвращаем отсортированный список рецептов
        // по заданному полю сортировки
        val sortedRecipes = when (params.sortBy) {
            SortBy.NAME -> params.unsortedRecipes.sortedBy { it.name }
            SortBy.LAST_UPDATED -> params.unsortedRecipes.sortedByDescending { it.lastUpdated }
        }
        return Either.Right(sortedRecipes)
    }
}
