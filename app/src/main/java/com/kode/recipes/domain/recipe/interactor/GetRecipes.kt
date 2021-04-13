package com.kode.recipes.domain.recipe.interactor

import com.kode.recipes.domain.base.interactor.None
import com.kode.recipes.domain.base.interactor.UseCase
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.repository.RecipeRepository
import javax.inject.Inject

class GetRecipes
@Inject constructor(private val recipeRepository: RecipeRepository) :
    UseCase<List<Recipe>, None>() {
    override suspend fun run(params: None) = recipeRepository.getRecipes()
}