package com.kode.recipes.data.recipe.converter

import com.kode.recipes.data.recipe.model.RecipeDto
import com.kode.recipes.domain.recipe.Recipe

fun RecipeDto.toRecipe() = Recipe(
    uuid,
    name,
    images[0],
    description,
    instructions,
    difficulty
)

/*
// Перевод даты в строку по Красноярскому часовому поясу
private fun Date.toStringDate(): String {
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))
    simpleDateFormat.timeZone = TimeZone.getTimeZone("Asia/Krasnoyarsk")
    return simpleDateFormat.format(this)
}*/
