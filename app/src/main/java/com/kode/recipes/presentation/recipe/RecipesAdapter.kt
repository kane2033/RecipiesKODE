package com.kode.recipes.presentation.recipe

import androidx.recyclerview.widget.DiffUtil
import com.kode.recipes.R
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.presentation.base.BaseListAdapter

class RecipesAdapter : BaseListAdapter<Recipe>(Companion) {

    companion object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem.uuid == newItem.uuid
    }

    override fun getItemViewType(position: Int) = R.layout.item_recipe

    // Неотсортированный список хранит изначальный список рецептов
    private var unfilteredList = listOf<Recipe>()

    // При добавлении списка рецептов,
    // сохраняется изначальный список
    override fun submitList(list: List<Recipe>?) {
        list?.let { unfilteredList = it }
        super.submitList(list)
    }

    // Когда требуется показать отсортированный список,
    // не перезаписываем unfilteredList
    private fun submitListFiltered(list: List<Recipe>?) {
        super.submitList(list)
    }
}