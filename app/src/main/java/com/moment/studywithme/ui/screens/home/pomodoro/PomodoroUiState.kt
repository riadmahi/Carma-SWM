package com.moment.studywithme.ui.screens.home.pomodoro

import com.moment.studywithme.domain.model.Task
import com.moment.studywithme.domain.model.UserPreferences

data class PomodoroUiState(
    val timer: String,
    val timerState: TimerState,
    val progress: Float,
    val tasks: MutableList<Task>,
    val userPreferences: UserPreferences
)

enum class TimerState {
    START,
    PLAY,
    PAUSE,
}