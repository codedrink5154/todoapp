package io.github.codedrink5154.todoapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.log
import kotlin.time.Duration.Companion.milliseconds

/* 计时器的运行状态 */
enum class TimerStatus {
    STOP, // 没有运行
    RUNNING, // 正在运行
    PAUSE, // 暂停
}

/* 专注计时器的状态 */
data class FocusTimerState(
    val totalDuration: Long,
    val remainingTime: Long = 25 * 60 * 1000L,
    val status: TimerStatus = TimerStatus.STOP,
    val isWorking: Boolean = true, // 番茄钟状态， true为工作
)

class FocusViewModel : ViewModel() {
    /* 目标时间，单位分钟 */
    private var _targetTime = MutableStateFlow<Int>(25)
    val targetTime: StateFlow<Int> = _targetTime.asStateFlow()

    /* 用户自行设置的倒计时时间，仅用于倒计时 */
    private var _countdownTime = MutableStateFlow(40)
    val countdownTime: StateFlow<Int> = _countdownTime.asStateFlow()

    /* 计时器状态 */
    private var _focusTimerState = MutableStateFlow(FocusTimerState(_targetTime.value * 60 * 1000L))
    val focusTimerState: StateFlow<FocusTimerState> = _focusTimerState.asStateFlow()

    /* 当前选中的模式 */
    private var _selectedMode = MutableStateFlow<Int>(0)
    val selectedMode: StateFlow<Int> = _selectedMode.asStateFlow()

    /* 计时器Job */
    private var timerJob: Job? = null

    /* 倒计时结束的时间，单位毫秒，内容是时间戳 */
    private var endTimeMillis = 0L

    /* 支持的计时模式 */
    val modes = listOf("番茄钟", "倒计时")

    /* 时间设置面板是否开启 */
    private var _editorOpen = MutableStateFlow(false)
    val editorOpen: StateFlow<Boolean> = _editorOpen.asStateFlow()

    /* 更换计时器模式, 并且重新设置目标时间 */
    fun updateMode(newMode: Int) {
        _targetTime.update { if (newMode == 0) 25 else _countdownTime.value }
        _focusTimerState.update {
            it.copy(
                remainingTime = _targetTime.value * 60 * 1000L,
                totalDuration = _targetTime.value * 60 * 1000L,
                status = TimerStatus.STOP,
                isWorking = true,
            )
        }
        _selectedMode.update { newMode }
    }

    /* 开始计时器 */
    fun startFocusTimer() {
        val state = _focusTimerState.value

        if (state.status == TimerStatus.RUNNING) return

        // 设定终止时间,必须先设置目标时间
        endTimeMillis = System.currentTimeMillis() + _focusTimerState.value.remainingTime

        // 更新计时器状态
        _focusTimerState.update {
            it.copy(status = TimerStatus.RUNNING)
        }

        // 取消可能正在运行的计时器
        timerJob?.cancel()

        // 创建协程环境并启动协程
        timerJob = viewModelScope.launch {
            while (true) {
                // 计算剩余时间
                val remaining = (
                        endTimeMillis - System.currentTimeMillis()
                        ).coerceAtLeast(0L)

                _focusTimerState.update {
                    it.copy(remainingTime = remaining)
                }

                // 截止时停止
                if (remaining == 0L) {
                    if (_selectedMode.value == 1) {
                        // 如果是倒计时模式，就直接停止
                        _focusTimerState.update {
                            it.copy(
                                remainingTime = it.totalDuration,
                                status = TimerStatus.STOP,
                            )
                        }
                    } else {
                        // 如果是番茄钟模式，则需要更新目标时间，并开启新的计时器
                        if (_focusTimerState.value.isWorking) {
                            _targetTime.value = 5
                            _focusTimerState.update {
                                it.copy(
                                    isWorking = false,
                                    remainingTime = _targetTime.value * 60 * 1000L,
                                    totalDuration = _targetTime.value * 60 * 1000L,
                                    status = TimerStatus.PAUSE
                                )
                            }
                        } else {
                            _targetTime.value = 25
                            _focusTimerState.update {
                                it.copy(
                                    isWorking = true,
                                    remainingTime = _targetTime.value * 60 * 1000L,
                                    totalDuration = _targetTime.value * 60 * 1000L,
                                    status = TimerStatus.PAUSE
                                )
                            }
                        }
                        _focusTimerState.value.status
                        startFocusTimer()
                    }
                    break
                }

                delay(200.milliseconds)
            }
        }
    }

    /* 暂停计时器 */
    fun pauseFocusTimer() {
        if (_focusTimerState.value.status != TimerStatus.RUNNING) return

        timerJob?.cancel()
        timerJob = null

        // 计算剩余时间
        val remaining = (
                endTimeMillis - System.currentTimeMillis()
                ).coerceAtLeast(0L)

        _focusTimerState.update {
            it.copy(
                remainingTime = remaining,
                status = TimerStatus.PAUSE,
            )
        }
    }

    /* 继续计时 */
    fun resumeFocusTimer() {
        if (_focusTimerState.value.status != TimerStatus.PAUSE) return
        startFocusTimer()
    }

    /* 重置计时 */
    fun stopFocusTimer() {
        if (_focusTimerState.value.status == TimerStatus.STOP) return

        timerJob?.cancel()
        timerJob = null

        _focusTimerState.update {
            it.copy(
                remainingTime = it.totalDuration,
                status = TimerStatus.STOP,
            )
        }

    }
}
