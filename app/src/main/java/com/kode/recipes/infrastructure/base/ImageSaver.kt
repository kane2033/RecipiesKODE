package com.kode.recipes.infrastructure.base

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageSaver {

    suspend fun saveToPictures(bitmap: Bitmap, contentResolver: ContentResolver) {
        withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) saveImageInQ(
                bitmap,
                contentResolver
            )
            else saveImageInLegacy(bitmap)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageInQ(bitmap: Bitmap, contentResolver: ContentResolver) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, getFileName())
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }

        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?.let { imageUri ->
                val outputStream = contentResolver.openOutputStream(imageUri)
                outputStream.use { bitmap.compress(it) }

                contentValues.clear()
                contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
                contentResolver.update(imageUri, contentValues, null, null)
            }
    }

    private fun saveImageInLegacy(bitmap: Bitmap) {
        @Suppress("DEPRECATION")
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, getFileName())
        FileOutputStream(image).use { bitmap.compress(it) }
    }

    private fun Bitmap.compress(fileOutputStream: OutputStream?) {
        compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream)
    }

    private fun getFileName() = "IMG_${System.currentTimeMillis()}.jpg"

}