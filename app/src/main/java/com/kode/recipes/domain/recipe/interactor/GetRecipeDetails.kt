package com.kode.recipes.domain.recipe.interactor

import com.kode.recipes.domain.base.interactor.UseCase
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeDetails
@Inject constructor(private val recipeRepository: RecipeRepository) :
    UseCase<Recipe, String>() {
    override suspend fun run(params: String) = recipeRepository.getRecipeDetails(params)
}