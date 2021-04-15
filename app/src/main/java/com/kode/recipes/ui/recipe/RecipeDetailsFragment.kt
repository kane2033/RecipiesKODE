package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.*
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipeDetailsBinding
import com.kode.recipes.presentation.recipe.RecipesViewModel
import com.kode.recipes.presentation.recipe.SwipeImageAdapter
import com.kode.recipes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipeDetailsFragment : BaseFragment(R.layout.fragment_recipe_details) {

    override val viewModel: RecipesViewModel by hiltNavGraphViewModels(R.id.recipesMasterDetailGraph)

    private val binding: FragmentRecipeDetailsBinding by viewBinding(FragmentRecipeDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@RecipeDetailsFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

            imageViewPager.adapter = SwipeImageAdapter()
            TabLayoutMediator(imageCountTabLayout, imageViewPager) { _, _ -> }.attach()
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}