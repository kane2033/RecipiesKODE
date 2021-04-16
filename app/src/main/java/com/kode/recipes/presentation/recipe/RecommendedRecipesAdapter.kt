package com.kode.recipes.presentation.recipe

import androidx.recyclerview.widget.DiffUtil
import com.kode.recipes.R
import com.kode.recipes.domain.recipe.entity.RecipeBrief
import com.kode.recipes.presentation.base.BaseListAdapter

class RecommendedRecipesAdapter : BaseListAdapter<RecipeBrief>(Companion) {

    companion object : DiffUtil.ItemCallback<RecipeBrief>() {
        override fun areItemsTheSame(oldItem: RecipeBrief, newItem: RecipeBrief): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: RecipeBrief, newItem: RecipeBrief): Boolean =
            oldItem.uuid == newItem.uuid
    }

    override fun getItemViewType(position: Int) = R.layout.item_recommended_recipe
}