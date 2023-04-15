package com.kabos.silenttimer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<TimerUiState> = MutableStateFlow(TimerUiState.Default)
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun startTimer() {
        _uiState.update {
            it.copy(inProgress = true)
        }
        timerJob = viewModelScope.launch {
            while (_uiState.value.inProgress) {
                delay(1.seconds)
                _uiState.update {
                    it.copy(
                        elapsedTime = it.elapsedTime + 1
                    )
                }
            }
        }
    }

    fun stopTimer() {
        _uiState.update {
            it.copy(inProgress = false)
        }
        timerJob?.cancel()
    }
}

/**
 * @param setTimerSecond 指定されたタイマーの時間
 * @param elapsedTime 経過時間
 */
data class TimerUiState(
    val inProgress: Boolean = false,
    val setTimerSecond: Int = 30,
    val elapsedTime: Int = 0,
) {

    // todo 残り時間を算出する
    val displayElapsedTime: String = Time(elapsedTime).toString()
    val indicatorProgress: Float = (elapsedTime / setTimerSecond.toFloat())

    companion object {
        val Default = TimerUiState()
    }
}

@JvmInline
value class Time(private val value: Int) {
    override fun toString(): String {
        return value.toString()
    }
}
