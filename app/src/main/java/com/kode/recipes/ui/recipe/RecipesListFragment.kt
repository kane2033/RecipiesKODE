package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipesListBinding
import com.kode.recipes.presentation.recipe.RecipesAdapter
import com.kode.recipes.presentation.recipe.RecipesViewModel
import com.kode.recipes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesListFragment : BaseFragment(R.layout.fragment_recipes_list) {

    override val viewModel: RecipesViewModel by hiltNavGraphViewModels(R.id.recipesMasterDetailGraph)

    private val binding: FragmentRecipesListBinding by viewBinding(FragmentRecipesListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.adapter = RecipesAdapter()

        observeNavigation()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_list, menu)

        val searchView = menu.findItem(R.id.searchButton).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Фильтрация при каждом вводе символа
            override fun onQueryTextChange(newText: String?): Boolean {
                binding.adapter?.filter?.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String?) = false
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.sortByButton) {
            navigateTo(R.id.action_recipesListFragment_to_sortByBottomSheetFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}