package com.exercise.summarysix.ui

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.exercise.summarysix.R
import com.exercise.summarysix.Resource.Resources
import com.exercise.summarysix.databinding.FragmentSecurityScreenBinding
import com.exercise.summarysix.ui.SecurityPageViewModel.SecurityPageViewModel
import kotlinx.coroutines.launch

class SecurityScreenFragment :
    BaseFragment<FragmentSecurityScreenBinding>(FragmentSecurityScreenBinding::inflate) {
    private val viewModel: SecurityPageViewModel by viewModels()

    override fun setUp() {
        setButtonClickListeners()
        observePasscodeResult()
        observePasscodeViewsState()
    }

    private fun setButtonClickListeners() {
        val buttons = arrayOf(
            binding.btn01, binding.btn02, binding.btn03,
            binding.btn04, binding.btn05, binding.btn06,
            binding.btn07, binding.btn08, binding.btn09,
            binding.btn00
        )

        for ((index, button) in buttons.withIndex()) {
            button.setOnClickListener {
                viewModel.handleButtonClick(button.text.toString())
//                viewModel.updatePasscodeViewsState(index)
            }
        }

        binding.btnClear.setOnClickListener {
            viewModel.clearPasscode()
            viewModel.resetPasscodeViewsState()
        }
    }

    private fun observePasscodeResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.passcodeResult.collect { result ->
                    when (result) {
                        is Resources.SUCCESS -> showToast("Success")
                        is Resources.ERROR -> showToast(result.message ?: "An error occurred")
                        else -> {}
                    }
                }
            }
        }
    }

    private fun observePasscodeViewsState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.passcodeViewsState.collect { state ->
                updatePasscodeViews(state)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updatePasscodeViews(state: List<Boolean>) {
        val views = arrayOf(
            binding.view01, binding.view02,
            binding.view03, binding.view04
        )

        for ((i, view) in views.withIndex()) {
            if (i < state.size) {
                view.setBackgroundResource(
                    if (state[i]) R.drawable.view_green_oval
                    else R.drawable.view_gray_oval
                )
            }
        }
    }
}