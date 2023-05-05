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
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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
                // 0.1sec毎に更新
                delay(10.milliseconds)
                _uiState.update {
                    it.copy(
                        elapsedTime = it.elapsedTime + 10f
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
    val setTimerSecond: Float = 10_000f,
    val elapsedTime: Float = 0f,
) {

    val remainingTime: Time = Time.calcRemainingTime(
        setTime = setTimerSecond,
        elapsedTime = elapsedTime,
    )

    val indicatorProgress: Float = (elapsedTime / setTimerSecond)

    val isFinished: Boolean = elapsedTime >= setTimerSecond

    companion object {
        val Default = TimerUiState()
    }
}

@JvmInline
value class Time(private val value: Int) {
    override fun toString(): String {
        return  convertTommss()
    }

    private fun convertTommss(): String {
        val duration = (this.value * 1_000).toDuration(DurationUnit.MILLISECONDS)
        return duration.toComponents { minutes, seconds, _ ->
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    companion object {
        fun calcRemainingTime(setTime: Float, elapsedTime: Float): Time {
            val remainingTime = setTime - elapsedTime
            return if (remainingTime > 0) {
                Time((remainingTime / 1000f).toInt())
            } else {
                Time(0)
            }
        }
    }
}
