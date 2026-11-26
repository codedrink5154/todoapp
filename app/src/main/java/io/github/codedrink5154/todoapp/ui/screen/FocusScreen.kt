package io.github.codedrink5154.todoapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Start
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FocusScreen(innerPadding: PaddingValues) {
    /* 目标时间 */
    var targetTime by rememberSaveable { mutableStateOf<Int>(25) }
    /* 剩余时间 */
    var remainTime by rememberSaveable { mutableStateOf<Int>(25) }
    /* 是否正在计时 */
    var isTicking by rememberSaveable { mutableStateOf(false) }
    /* 支持的计时模式 */
    val modes = listOf("番茄钟", "倒计时")
    /* 当前选中的模式 */
    var selectedMode by rememberSaveable { mutableStateOf(0) }

    @Composable
    fun TimeControlButtonRow() {
        val controlIcon = if (isTicking) Icons.Default.Pause else Icons.Default.PlayArrow
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            /* 设置按钮 */
            IconButton(
                onClick = {},
            ) {
                Icon(Icons.Default.Settings, "")
            }
            /* 终止专注按钮 */
            IconButton(
                onClick = {},
                enabled = isTicking,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Stop, "")
            }
            /* 开始与暂停按钮 */
            IconButton(
                onClick = { isTicking = !isTicking },
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
            Text(
                text = "剩余",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = "$remainTime min",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "目标: $targetTime min",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(Modifier.height(15.dp))
            TimeControlButtonRow()
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
                    onClick = { selectedMode = index },
                )
            }
        }
    }
}
