package com.kode.recipes.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kode.recipes.R
import com.kode.recipes.databinding.FragmentFailureBinding
import com.kode.recipes.presentation.base.FailureViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Диалоговый фрагмент, ответственный за отображение информации об ошибке
 * и предоставление повтора операции, из-за которой возникла данная ошибка.
 * */
@AndroidEntryPoint
class FailureFragment : DialogFragment(R.layout.fragment_failure) {

    private val viewModel: FailureViewModel by viewModels()

    private val binding: FragmentFailureBinding by viewBinding(FragmentFailureBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Отображаем диалог в фуллскрине
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_DayNight_DialogWhenLarge)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@FailureFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        binding.retryButton.setOnClickListener {
            viewModel.failureInfo.value?.retryClickedCallback?.invoke()
            dismissAllowingStateLoss()
        }
    }
}