package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipesListBinding
import com.kode.recipes.presentation.base.ItemClickListener
import com.kode.recipes.presentation.recipe.RecipesAdapter
import com.kode.recipes.presentation.recipe.RecipesViewModel
import com.kode.recipes.ui.base.BaseFragment

class RecipesListFragment : BaseFragment(R.layout.fragment_recipes_list) {

    override val viewModel: RecipesViewModel by navGraphViewModels(R.id.recipesMasterDetailGraph) {
        defaultViewModelProviderFactory
    }

    private val binding: FragmentRecipesListBinding by viewBinding(FragmentRecipesListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.root

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.adapter = RecipesAdapter(ItemClickListener {
            viewModel.selectedRecipe.value = it
            navigateTo(R.id.action_recipesListFragment_to_recipeDetailsFragment)
        })
    }
}