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
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun SilentTimerAppHolder(
    viewModel: TimerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    SilentTimerApp(
        uiState = uiState,
        startTimer = viewModel::startTimer,
        stopTimer = viewModel::stopTimer,
    )
}

@Composable
fun SilentTimerApp(
    uiState: TimerUiState,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
) {
    val currentTimerState by remember { mutableStateOf(0f) }

    val animatedProgress by animateFloatAsState(
        targetValue = currentTimerState,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )

    Scaffold(
        timeText = { TimeText() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Indicator(progress = animatedProgress)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CurrentTimeText(countText = uiState.displayElapsedTime)
                StartButton(
                    inProgress = uiState.inProgress,
                    startTimer = startTimer,
                    stopTimer = stopTimer,
                )
            }
        }
    }
}

@Composable
private fun Indicator(progress: Float) {
    CircularProgressIndicator(
        progress = progress,
        modifier = Modifier.fillMaxSize(),
        startAngle = 290f,
        endAngle = 250f,
        strokeWidth = 10.dp
    )
}

@Composable
private fun StartButton(
    inProgress: Boolean,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
) {
    Button(
        modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
        onClick = {
            if (inProgress) stopTimer() else startTimer()
        },
    ) {
        Icon(
            imageVector = if (inProgress) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
            contentDescription = null,
        )
    }
}

@Composable
private fun CurrentTimeText(countText: String) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = countText,
        style = MaterialTheme.typography.title1
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreviews() {
    SilentTimerApp(
        uiState = TimerUiState(
            inProgress = false,
            setTimerSecond = 0,
            elapsedTime = 10
        ),
        startTimer = {},
        stopTimer = {}
    )
}
