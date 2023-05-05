package com.kabos.silenttimer.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Square
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ProgressIndicatorDefaults
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.kabos.silenttimer.component.FlashSurface
import com.kabos.silenttimer.presentation.theme.Red200
import com.kabos.silenttimer.presentation.theme.SilentTimerTheme

@Composable
fun SilentTimerAppHolder(
    viewModel: TimerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.indicatorProgress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )

    SilentTimerApp(
        uiState = uiState,
        indicatorProgress = animatedProgress,
        startTimer = viewModel::startTimer,
        stopTimer = viewModel::stopTimer,
    )
}

@Composable
private fun SilentTimerApp(
    uiState: TimerUiState,
    indicatorProgress: Float,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
) {
    Scaffold(timeText = { TimeText() }) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isFinished) {
                FinisTimerApp()
            } else {
                InProgressTimerApp(
                    uiState = uiState,
                    indicatorProgress = indicatorProgress,
                    startTimer = startTimer,
                    stopTimer = stopTimer,
                )
            }
        }
    }
}

/**
 * 進行中のタイマー画面
 */
@Composable
private fun InProgressTimerApp(
    uiState: TimerUiState,
    indicatorProgress: Float,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
) {
    Indicator(progress = indicatorProgress)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DisplaySeconds(currentValue = uiState.remainingTime)
        StartStopButton(
            inProgress = uiState.inProgress,
            startTimer = startTimer,
            stopTimer = stopTimer,
        )
    }
}

/**
 * タイマー完了時の画面
 */
@Composable
private fun FinisTimerApp() {
    Indicator(
        progress = 1f,
        indicatorColor = Red200,
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FlashSurface { modifier ->
            DisplaySeconds(
                currentValue = Time(0),
                color = Red200,
                modifier = modifier,
            )
        }

        Button(
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Red200,
                contentColor = Color.White,
            ),
            onClick = { /* TODO */ },
        ) {
            Icon(
                imageVector = Icons.Rounded.Square,
                contentDescription = null,
            )
        }
    }
}

/** component */

@Composable
private fun Indicator(
    progress: Float,
    indicatorColor: Color = MaterialTheme.colors.primary,
) {
    CircularProgressIndicator(
        progress = progress,
        modifier = Modifier.fillMaxSize(),
        indicatorColor = indicatorColor,
        startAngle = 290f,
        endAngle = 250f,
        strokeWidth = 10.dp,
    )
}

@Composable
private fun StartStopButton(
    inProgress: Boolean,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
) {
    Button(
        modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
        onClick = { if (inProgress) stopTimer() else startTimer() },
    ) {
        Icon(
            imageVector = if (inProgress) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
            contentDescription = null,
        )
    }
}

@Composable
private fun DisplaySeconds(
    currentValue: Time,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = color,
        text = currentValue.toString(),
        style = MaterialTheme.typography.title1,
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
private fun DefaultPreviews() {
    SilentTimerApp(
        uiState = TimerUiState(
            inProgress = false,
            setTimerSecond = 50f,
            elapsedTime = 10f,
        ),
        indicatorProgress = 0.5f,
        startTimer = {},
        stopTimer = {},
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
private fun PreviewFinish() {
    SilentTimerTheme {
        FinisTimerApp()
    }
}
