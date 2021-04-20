package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentRecipeDetailsBinding
import com.kode.recipes.presentation.base.ItemClickedInterface
import com.kode.recipes.presentation.recipe.RecipeDetailsConstants
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
            imageViewPager.adapter = SwipeImageAdapter(imageClickedInterface)
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

    private val imageClickedInterface = ItemClickedInterface<String> {
        // Открываем полноэкранный фрагмент с передачей данных
        findNavController().navigate(
            R.id.action_recipeDetailsFragment_to_recipeImagesFullScreenFragment,
            // Переносим в полноэкранный фрагмент ссылки на картинки и индекс выбранной картинки
            bundleOf(
                RecipeDetailsConstants.URLS_KEY to viewModel.recipeDetails.value?.imagesUrls?.toTypedArray(),
                RecipeDetailsConstants.INDEX_KEY to binding.imageCountTabLayout.selectedTabPosition
            ),
            null,
            // Не работает с viewpager :(
            FragmentNavigatorExtras(binding.imageViewPager to RecipeDetailsConstants.SHARED_VIEW_PAGER_KEY)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareButton -> {
                makeToast(R.string.in_development)
            }
            R.id.favoriteButton -> {
                makeToast(R.string.in_development)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}