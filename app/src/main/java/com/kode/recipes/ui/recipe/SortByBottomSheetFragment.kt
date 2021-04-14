package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kode.recipes.R
import com.kode.recipes.databinding.BottomSheetSortByBinding
import com.kode.recipes.domain.recipe.entity.SortBy
import com.kode.recipes.presentation.recipe.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Диалоговый фрагмент, появляющийся снизу и позволяющий
 * отсортировать рецепты по имени или по дате.
 * */
@AndroidEntryPoint
class SortByBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel: RecipesViewModel by hiltNavGraphViewModels(R.id.recipesMasterDetailGraph)

    private val binding: BottomSheetSortByBinding by viewBinding(BottomSheetSortByBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_sort_by, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            sortByNameButton.setOnClickListener {
                dismissAllowingStateLoss()
                viewModel.sortRecipes(SortBy.NAME)
            }
            sortByDateButton.setOnClickListener {
                dismissAllowingStateLoss()
                viewModel.sortRecipes(SortBy.LAST_UPDATED)
            }
        }
    }
}