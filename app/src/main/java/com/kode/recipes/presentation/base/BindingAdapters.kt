package com.kode.recipes.presentation.base

import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

object BindingAdapters {

    @JvmStatic
    @BindingAdapter(value = ["imageUrl"])
    fun ImageView.setImageFromUrl(url: String?) {
        if (url != null && url.isNotBlank()) {
            //если есть картинка
            //устанавливаем картинку из url в imageView форматом Bitmap
            Picasso.get().load(url).into(this)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["loading"])
    fun ContentLoadingProgressBar.setLoading(isLoading: Boolean) {
        if (isLoading) this.show() else this.hide()
    }

    @JvmStatic
    @BindingAdapter(value = ["adapter"])
    fun RecyclerView.bindRecyclerViewAdapter(adapter: BaseListAdapter<*>) {
        this.run {
            this.setHasFixedSize(true) // с осторожностью
            this.adapter = adapter
        }
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter(value = ["items"])
    fun <T> RecyclerView.setItems(items: List<T>?) {
        (this.adapter as? BaseListAdapter<T>)?.submitList(items)
    }
}