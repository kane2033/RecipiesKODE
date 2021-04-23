package com.kode.recipes.domain.recipe.repository

import android.graphics.Bitmap

interface ImageRepository {
    suspend fun downloadImageFromUrl(url: String): Bitmap?
    suspend fun saveBitmapToPictures(bitmap: Bitmap)
}