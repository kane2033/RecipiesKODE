package com.kode.recipes.domain.recipe.entity

import java.text.SimpleDateFormat
import java.util.*

data class Recipe(
    val uuid: String,
    val name: String,
    val imageUrl: String,
    val lastUpdated: Date,
    val description: String?,
    val instructions: String?,
    val difficulty: Int,
    val similar: List<RecipeBrief>?
) {
    val lastUpdatedSimple: String =
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(lastUpdated)
}