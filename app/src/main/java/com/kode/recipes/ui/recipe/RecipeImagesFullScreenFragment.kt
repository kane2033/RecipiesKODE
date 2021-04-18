package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipeImagesFullscreenBinding
import com.kode.recipes.infrastructure.base.ImageSaver
import com.kode.recipes.presentation.recipe.RecipeImagesFullScreenViewModel
import com.kode.recipes.presentation.recipe.SwipeImageAdapter
import com.kode.recipes.ui.base.BaseFragment
import kotlinx.coroutines.launch

class RecipeImagesFullScreenFragment : BaseFragment(R.layout.fragment_recipe_images_fullscreen) {

    override val viewModel: RecipeImagesFullScreenViewModel by viewModels()

    private val binding: FragmentRecipeImagesFullscreenBinding by viewBinding(
        FragmentRecipeImagesFullscreenBinding::bind
    )

    private val args: RecipeImagesFullScreenFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Не работает с viewpager :(
        //sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        binding.apply {
            viewModel = this@RecipeImagesFullScreenFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

            imageViewPager.adapter = SwipeImageAdapter { }
            TabLayoutMediator(imageCountTabLayout, imageViewPager) { _, _ -> }.attach()
        }

        viewModel.imagesUrls.observe(viewLifecycleOwner, {
            // Установка той же картинки, что была выбрана в прошлом фрагменте
            val selectedImageIndex = args.index
            binding.imageViewPager.setCurrentItem(selectedImageIndex, false)
        })
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_images, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.saveButton) {
            binding.apply {
                // Получаем viewholder отображаемого view
                val currentViewHolder =
                    (imageViewPager[0] as RecyclerView).findViewHolderForAdapterPosition(
                        imageCountTabLayout.selectedTabPosition
                    )
                // Получаем imageview с отображаемой картинкой
                val imageView = currentViewHolder?.itemView?.findViewById<ImageView>(R.id.imageView)
                // Сохраняем в галерею
                imageView?.saveImageToPictures()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Сохраняем картинку в галарею (папка Pictures)
    private fun ImageView.saveImageToPictures() {
        val bitmap = drawable.toBitmap()
        lifecycleScope.launch {
            ImageSaver.saveToPictures(bitmap, requireActivity().applicationContext.contentResolver)
            makeToast(R.string.downloaded)
        }
    }
}