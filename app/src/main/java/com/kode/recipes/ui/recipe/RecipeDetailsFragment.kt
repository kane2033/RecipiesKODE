package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipeDetailsBinding
import com.kode.recipes.presentation.recipe.RecipesViewModel
import com.kode.recipes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailsFragment : BaseFragment(R.layout.fragment_recipe_details) {

    override val viewModel: RecipesViewModel by hiltNavGraphViewModels(R.id.recipesMasterDetailGraph)

    private val binding: FragmentRecipeDetailsBinding by viewBinding(FragmentRecipeDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}