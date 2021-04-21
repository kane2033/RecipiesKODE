package com.kode.recipes.domain.recipe.interactor

import com.kode.recipes.domain.base.exception.Failure
import com.kode.recipes.domain.base.functional.Either
import com.kode.recipes.domain.base.interactor.UseCase
import com.kode.recipes.domain.recipe.exception.ImageDownloadFailure
import com.kode.recipes.domain.recipe.repository.ImageRepository
import javax.inject.Inject

class SaveImageToPictures
@Inject constructor(private val imageRepository: ImageRepository) :
    UseCase<Boolean, String>() {
    override suspend fun run(params: String): Either<Failure, Boolean> {
        val bitmap = imageRepository.downloadImageFromUrl(params)
            ?: return Either.Left(ImageDownloadFailure)

        imageRepository.saveBitmapToPictures(bitmap)
        return Either.Right(true)
    }
}