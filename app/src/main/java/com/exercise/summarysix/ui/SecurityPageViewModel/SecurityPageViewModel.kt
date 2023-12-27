package com.exercise.summarysix.ui.SecurityPageViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.summarysix.Resource.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SecurityPageViewModel: ViewModel() {
    private val _passcodeResult: MutableStateFlow<Resources<Unit>?> = MutableStateFlow(null)
    val passcodeResult: StateFlow<Resources<Unit>?> get() = _passcodeResult

    private val _passcodeViewsState = MutableStateFlow<List<Boolean>>(List(4) { false })
    val passcodeViewsState: StateFlow<List<Boolean>> get() = _passcodeViewsState

    private val correctPasscode = "0934"
    private var enteredPasscode = ""

    fun handleButtonClick(value: String) {
        if (_passcodeViewsState.value?.none { it } == true) {
            resetPasscodeViewsState()
            enteredPasscode = "" //
        }

        updatePasscodeViewsState()

        if ((_passcodeViewsState.value.count { it } ?: 0) == 4) {
            enteredPasscode += value //
            checkPasscodeWithServer()
        }
    }

    fun clearPasscode() {
        resetPasscodeViewsState()
        enteredPasscode = ""
    }

    fun updatePasscodeViewsState() {
        val updatedState = _passcodeViewsState.value.toMutableList()
        val firstFalseIndex = updatedState.indexOfFirst { !it }
        updatedState[firstFalseIndex] = true
        _passcodeViewsState.value = updatedState
    }

    fun resetPasscodeViewsState() {
        _passcodeViewsState.value = List(4) { false }
    }

    fun checkPasscodeWithServer() {
        viewModelScope.launch {
            _passcodeResult.value = Resources.LOADING(true)
            try {
                withContext(Dispatchers.IO) {
                    Thread.sleep(1000)
                }

                if (enteredPasscode == correctPasscode) {
                    _passcodeResult.value = Resources.SUCCESS(Unit)
                } else {
                    _passcodeResult.value = Resources.ERROR(message = "Incorrect passcode. Please try again.")
                    clearPasscode()
                }
            } catch (e: Exception) {
                _passcodeResult.value = Resources.ERROR(message = "An error occurred")
            } finally {
                _passcodeResult.value = Resources.LOADING(false)
            }
        }
    }
}