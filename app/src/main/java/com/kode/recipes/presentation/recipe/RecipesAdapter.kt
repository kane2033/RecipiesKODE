package com.kode.recipes.presentation.recipe

import androidx.recyclerview.widget.DiffUtil
import com.kode.recipes.R
import com.kode.recipes.domain.recipe.Recipe
import com.kode.recipes.presentation.base.BaseListAdapter
import com.kode.recipes.presentation.base.ItemClickListener

class RecipesAdapter(clickListener: ItemClickListener<Recipe>) :
    BaseListAdapter<Recipe>(clickListener, Companion) {

    companion object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem.uuid == newItem.uuid
    }

    override fun getItemViewType(position: Int) = R.layout.item_recipe
}