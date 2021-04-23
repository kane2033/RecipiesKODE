package com.kode.recipes.ui.recipe

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipeImagesFullscreenBinding
import com.kode.recipes.domain.base.entity.FailureInfo
import com.kode.recipes.domain.recipe.exception.ImageDownloadFailure
import com.kode.recipes.presentation.recipe.RecipeDetailsConstants
import com.kode.recipes.presentation.recipe.RecipeImagesFullScreenViewModel
import com.kode.recipes.presentation.recipe.SwipeImageAdapter
import com.kode.recipes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeImagesFullScreenFragment : BaseFragment(R.layout.fragment_recipe_images_fullscreen) {

    override val viewModel: RecipeImagesFullScreenViewModel by viewModels()

    private val binding: FragmentRecipeImagesFullscreenBinding by viewBinding(
        FragmentRecipeImagesFullscreenBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Не работает с viewpager :(
        //sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        binding.apply {
            viewModel = this@RecipeImagesFullScreenFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

            imageViewPager.adapter = SwipeImageAdapter()
            TabLayoutMediator(imageCountTabLayout, imageViewPager) { _, _ -> }.attach()
        }

        viewModel.imagesUrls.observe(viewLifecycleOwner, {
            // Установка той же картинки, что была выбрана в прошлом фрагменте
            val selectedImageIndex = arguments?.getInt(RecipeDetailsConstants.INDEX_KEY) ?: 0
            binding.imageViewPager.setCurrentItem(selectedImageIndex, false)
        })

        // Если картинка успешно сохранилась, оповещаем тостом
        viewModel.isImageSaved.observe(viewLifecycleOwner, { event ->
            if (event.getContentIfNotHandled() == true) {
                makeToast(R.string.downloaded)
                openGalleryChooser()
            }
        })

        setHasOptionsMenu(true)

        val imageDownloadFailureInfo = FailureInfo(
            viewModel::saveImageToPictures,
            getString(R.string.error_image_download_title),
            getString(R.string.error_image_download),
            getString(R.string.error_image_download_retry)
        )

        handleFailure(
            baseRetryClickedCallback = viewModel::saveImageToPictures,
            handleFailure = {
                when (it) {
                    is ImageDownloadFailure -> imageDownloadFailureInfo
                    else -> null
                }
            })
    }

    private fun openGalleryChooser() {
        val chooser = Intent.createChooser(
            Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_GALLERY),
            getString(R.string.chooser_gallery)
        )

        try {
            startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            makeToast(R.string.error_chooser_gallery)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_images, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.saveButton) {
            viewModel.saveImageToPictures(binding.imageViewPager.currentItem)
        }
        return super.onOptionsItemSelected(item)
    }
}