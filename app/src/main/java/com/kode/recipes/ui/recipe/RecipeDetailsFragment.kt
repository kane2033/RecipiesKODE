package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipeDetailsBinding
import com.kode.recipes.presentation.recipe.RecipesViewModel
import com.kode.recipes.ui.base.BaseFragment

class RecipeDetailsFragment : BaseFragment(R.layout.fragment_recipe_details) {

    override val viewModel: RecipesViewModel by navGraphViewModels(R.id.recipesMasterDetailGraph) {
        defaultViewModelProviderFactory
    }

    private val binding: FragmentRecipeDetailsBinding by viewBinding(FragmentRecipeDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}