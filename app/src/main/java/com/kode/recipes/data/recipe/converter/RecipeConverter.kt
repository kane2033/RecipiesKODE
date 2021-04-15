package com.kode.recipes.data.recipe.converter

import com.kode.recipes.data.recipe.model.RecipeBriefDto
import com.kode.recipes.data.recipe.model.RecipeDto
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.domain.recipe.entity.RecipeBrief
import java.util.*

fun RecipeDto.toRecipe() = Recipe(
    uuid,
    name,
    images,
    Date(lastUpdated.toLong() * 1000),
    description,
    instructions,
    difficulty,
    similar?.map { it.toRecipeBrief() }
)

fun RecipeBriefDto.toRecipeBrief() = RecipeBrief(uuid, name)

/*
// Перевод даты в строку по Красноярскому часовому поясу
private fun Date.toStringDate(): String {
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))
    simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Krasnoyarsk")
    return simpleDateFormat.format(this)
}*/
