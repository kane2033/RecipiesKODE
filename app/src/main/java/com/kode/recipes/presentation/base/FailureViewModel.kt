package com.kode.recipes.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.kode.recipes.domain.base.entity.FailureInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FailureViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        const val FAILURE_INFO_KEY = "failure info"
    }

    val failureInfo: LiveData<FailureInfo> =
        savedStateHandle.getLiveData(FAILURE_INFO_KEY)
}