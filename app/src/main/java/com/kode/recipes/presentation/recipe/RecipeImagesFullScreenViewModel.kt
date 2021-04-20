package com.kode.recipes.presentation.recipe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.kode.recipes.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeImagesFullScreenViewModel @Inject constructor(savedStateHandle: SavedStateHandle) :
    BaseViewModel() {

    val imagesUrls = liveData<List<String>> {
        val urls =
            savedStateHandle.get<Array<String>>(RecipeDetailsConstants.URLS_KEY)?.toList()
        emit(urls ?: emptyList())
    }
}