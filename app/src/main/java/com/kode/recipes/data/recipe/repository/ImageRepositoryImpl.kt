package com.kode.recipes.data.recipe.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.kode.recipes.domain.recipe.repository.ImageRepository
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class ImageRepositoryImpl
@Inject constructor(@ApplicationContext private val applicationContext: Context) : ImageRepository {

    override suspend fun downloadImageFromUrl(url: String): Bitmap? {
        // Picasso требует запуска методе в главном потоке, а метод репозитория
        // вызывается в рабочем потоке, поэтому используем Dispatchers.Main
        return withContext(Dispatchers.Main) {
            // suspendCoroutine помогает вернуть значение из коллбэк методов
            suspendCoroutine { continuation ->
                Picasso.get().load(url).into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        continuation.resumeWith(Result.success(bitmap))
                    }

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                        e?.printStackTrace()
                        continuation.resumeWith(Result.success(null))
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                })
            }
        }

    }

    override suspend fun saveBitmapToPictures(bitmap: Bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageInQ(bitmap, applicationContext.contentResolver)
        } else {
            saveImageInLegacy(bitmap)
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