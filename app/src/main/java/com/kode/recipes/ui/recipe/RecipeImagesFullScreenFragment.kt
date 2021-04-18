package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipeImagesFullscreenBinding
import com.kode.recipes.presentation.recipe.RecipeImagesFullScreenViewModel
import com.kode.recipes.presentation.recipe.SwipeImageAdapter
import com.kode.recipes.ui.base.BaseFragment

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
            // download image
        }
        return super.onOptionsItemSelected(item)
    }
}