package com.kode.recipes.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kode.recipes.R
import com.kode.recipes.databinding.BottomSheetSearchByBinding
import com.kode.recipes.domain.recipe.entity.SearchBy
import com.kode.recipes.presentation.recipe.RecipesListViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Диалоговый фрагмент, появляющийся снизу и позволяющий
 * выбрать, по какому полю необходимо совершить поиск.
 * */
@AndroidEntryPoint
class SearchByBottomSheetDialog : BottomSheetDialogFragment() {

    private val viewModel: RecipesListViewModel by hiltNavGraphViewModels(R.id.recipesMasterDetailGraph)

    private val binding: BottomSheetSearchByBinding by viewBinding(BottomSheetSearchByBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.bottom_sheet_search_by, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            searchByNameButton.setOnClickListener {
                dismissAllowingStateLoss()
                viewModel.searchBy = SearchBy.NAME
            }
            searchByDescription.setOnClickListener {
                dismissAllowingStateLoss()
                viewModel.searchBy = SearchBy.DESCRIPTION
            }
            searchByInstructionsButton.setOnClickListener {
                dismissAllowingStateLoss()
                viewModel.searchBy = SearchBy.INSTRUCTIONS
            }
        }
    }
}