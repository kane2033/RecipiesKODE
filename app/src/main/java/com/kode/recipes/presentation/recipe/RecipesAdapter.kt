package com.kode.recipes.presentation.recipe

import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import com.kode.recipes.R
import com.kode.recipes.domain.recipe.entity.Recipe
import com.kode.recipes.presentation.base.BaseListAdapter
import java.util.*

class RecipesAdapter : BaseListAdapter<Recipe>(Companion), Filterable {

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

    override fun getFilter() = filter

    // Реализация filter по имени
    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<Recipe>()
            val defaultLocale = Locale.getDefault()

            if (constraint == null || constraint.isBlank()) {
                // Не осуществляем поиск, если запрос пуст
                filteredList.addAll(unfilteredList)
            } else {
                // Осуществление поиска (по полю name)
                val filterPattern = constraint.toString().toLowerCase(defaultLocale).trim()
                unfilteredList.forEach {
                    if (it.name.toLowerCase(defaultLocale).contains(filterPattern)) {
                        filteredList.add(it)
                    }
                }
            }

            return FilterResults().apply { values = filteredList }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            submitListFiltered(results?.values as? List<Recipe>)
        }

    }
}