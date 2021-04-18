package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipeDetailsBinding
import com.kode.recipes.presentation.recipe.RecipeDetailsViewModel
import com.kode.recipes.presentation.recipe.RecommendedRecipesAdapter
import com.kode.recipes.presentation.recipe.SwipeImageAdapter
import com.kode.recipes.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipeDetailsFragment : BaseFragment(R.layout.fragment_recipe_details) {

    override val viewModel: RecipeDetailsViewModel by viewModels()

    private val binding: FragmentRecipeDetailsBinding by viewBinding(FragmentRecipeDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@RecipeDetailsFragment.viewModel
            lifecycleOwner = viewLifecycleOwner

            recommendedRecyclerView.adapter = RecommendedRecipesAdapter()

            // При клике на картинку, она открывает в новом фрагменте на полном экране
            imageViewPager.adapter = SwipeImageAdapter(imageClickedInterface = {
                // Переносим в полноэкранный фрагмент ссылки на картинки и индекс выбранной картинки
                val urls =
                    this@RecipeDetailsFragment.viewModel.recipeDetails.value?.imagesUrls?.toTypedArray()
                val bundle = bundleOf(
                    "urls" to urls,
                    "index" to imageCountTabLayout.selectedTabPosition
                )
                findNavController().navigate(
                    R.id.action_recipeDetailsFragment_to_recipeImagesFullScreenFragment,
                    bundle,
                    null,
                    FragmentNavigatorExtras(imageViewPager to "viewPager") // Не работает с viewpager :(
                )
            })
            TabLayoutMediator(imageCountTabLayout, imageViewPager) { _, _ -> }.attach()
        }

        // При выборе нового рецепта из "Recommended", открывается такой же новый фрагмент
        // с передачей uuid
        viewModel.selectedRecipeUuid.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let { uuid ->
                navigateTo(RecipeDetailsFragmentDirections.actionRecipeDetailsFragmentSelf(uuid))
            }
        })

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}