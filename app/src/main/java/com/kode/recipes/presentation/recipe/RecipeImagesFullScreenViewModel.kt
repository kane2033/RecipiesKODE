package com.kode.recipes.presentation.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import com.kode.recipes.domain.base.functional.Event
import com.kode.recipes.domain.recipe.interactor.SaveImageToPictures
import com.kode.recipes.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeImagesFullScreenViewModel
@Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveImageToPictures: SaveImageToPictures
) :
    BaseViewModel() {

    val imagesUrls = liveData<List<String>> {
        val urls =
            savedStateHandle.get<Array<String>>(RecipeDetailsConstants.URLS_KEY)?.toList()
        emit(urls ?: emptyList())
    }

    private val _isImageSaved = MutableLiveData<Event<Boolean>>()
    val isImageSaved: LiveData<Event<Boolean>> = _isImageSaved

    // Сохранение картинки по url в папку Pictures
    fun saveImageToPictures(imageIndex: Int) {
        imagesUrls.value?.let { list ->
            saveImageToPictures(
                params = list[imageIndex],
                job = job,
                onResult = { it.fold(::handleFailure, ::handleImageSaved) }
            )
        }
    }

    private fun handleImageSaved(isSaved: Boolean) {
        _isImageSaved.value = Event(isSaved)
    }
}