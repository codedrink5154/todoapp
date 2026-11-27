package io.github.codedrink5154.todoapp.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.codedrink5154.todoapp.viewmodel.FocusViewModel
import io.github.codedrink5154.todoapp.viewmodel.TimerStatus

@ExperimentalMaterial3Api
@Composable
fun FocusScreen(innerPadding: PaddingValues, viewModel: FocusViewModel = viewModel()) {
    var focusTimeState = viewModel.focusTimerState.collectAsStateWithLifecycle().value
    val isTicking = focusTimeState.status
    val isWorking = focusTimeState.isWorking
    val selectedMode by viewModel.selectedMode.collectAsStateWithLifecycle()
    val modes = viewModel.modes
    val targetTime by viewModel.targetTime.collectAsStateWithLifecycle()

    val minutes = focusTimeState.remainingTime / 60_000
    val seconds = (focusTimeState.remainingTime % 60_000) / 1000

    @Composable
    fun TimeControlButtonRow() {
        val controlIcon = if (isTicking == TimerStatus.RUNNING) Icons.Default.Pause else Icons.Default.PlayArrow
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            /* 设置按钮 */
            IconButton(
                onClick = {},
                enabled = selectedMode == 1,
            ) {
                Icon(Icons.Default.Settings, "")
            }
            /* 终止专注按钮 */
            IconButton(
                onClick = { viewModel.stopFocusTimer() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Stop, "")
            }
            /* 开始与暂停按钮 */
            IconButton(
                onClick = {
                    when (isTicking) {
                        TimerStatus.STOP -> viewModel.startFocusTimer()
                        TimerStatus.PAUSE -> {
                            viewModel.resumeFocusTimer()
                        }

                        TimerStatus.RUNNING -> viewModel.pauseFocusTimer()
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(controlIcon, "")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val indicate = when (selectedMode) {
                0 -> if (isWorking) "工作" else "休息"
                1 -> "工作"
                else -> "剩余"
            }
            Text(
                text = indicate,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = "%02d:%02d".format(minutes, seconds),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 48.sp
            )
            Text(
                text = "目标: $targetTime min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(Modifier.height(15.dp))
            TimeControlButtonRow()
            LinearProgressIndicatorSample()
        }
        /* 模式选择 */
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.width(180.dp),
        ) {
            modes.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = modes.size
                    ),
                    label = { Text(label) },
                    selected = index == selectedMode,
                    onClick = { viewModel.updateMode(index) },
                    enabled = isTicking == TimerStatus.STOP
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun LinearProgressIndicatorSample(viewModel: FocusViewModel = viewModel()) {
    var state = viewModel.focusTimerState.collectAsStateWithLifecycle().value
    var remainingTime = state.remainingTime
    var totalTitme = if (state.totalDuration != 0L) state.totalDuration else 1
    val animatedProgress by animateFloatAsState(
        (1 - remainingTime / totalTitme.toFloat()),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LinearProgressIndicator(progress = { animatedProgress })
    }
}
