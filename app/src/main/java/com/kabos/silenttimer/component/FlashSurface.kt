package com.kabos.silenttimer.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [content]を点滅させる
 * [content]を非表示にした際に余白を維持するために、contentHeightを計算する
 * [Modifier.onGloballyPositioned]を利用してcontentの高さを計算するため、contentにmodifierをセットすること
 */
@Composable
fun FlashSurface(
    intervalMillis: Long = 500,
    isFlashing: Boolean = true,
    content: @Composable (modifier: Modifier) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val localDensity = LocalDensity.current
    var contentHeight by remember {
        mutableStateOf(0.dp)
    }
    var showContent by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(isFlashing) {
        coroutineScope.launch {
            while (isFlashing) {
                showContent = false
                delay(intervalMillis)
                showContent = true
                delay(intervalMillis)
            }
        }
    }

    if (showContent) {
        content(
            Modifier.onGloballyPositioned { coordinates ->
                contentHeight = with(localDensity) { coordinates.size.height.toDp() }
            }
        )
    } else {
        Spacer(modifier = Modifier.height(contentHeight))
    }
}
