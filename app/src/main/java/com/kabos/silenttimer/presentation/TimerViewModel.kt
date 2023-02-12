package com.kabos.silenttimer.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(): ViewModel() {

    private val _uiState: MutableStateFlow<TimerUiState> = MutableStateFlow(TimerUiState.Default)
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    fun startTimer() {
        _uiState.update {
            it.copy(inProgress = true)
        }
    }

    fun stopTimer() {
        _uiState.update {
            it.copy(inProgress = false)
        }
    }
}

data class TimerUiState(
    val inProgress: Boolean,
    val remainingTime: String,
) {
    companion object {
        val Default = TimerUiState(
            inProgress = false,
            remainingTime = "",
        )
    }
}
