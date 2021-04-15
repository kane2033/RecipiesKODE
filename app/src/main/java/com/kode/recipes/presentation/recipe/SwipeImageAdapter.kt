package com.kode.recipes.presentation.recipe

import androidx.recyclerview.widget.DiffUtil
import com.kode.recipes.R
import com.kode.recipes.presentation.base.BaseListAdapter

class SwipeImageAdapter : BaseListAdapter<String>(Companion) {

    companion object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    override fun getItemViewType(position: Int) = R.layout.item_image_swipable
}