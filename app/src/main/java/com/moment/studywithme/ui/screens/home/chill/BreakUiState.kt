package com.moment.studywithme.ui.screens.home.chill

import com.moment.studywithme.domain.model.UserPreferences
import com.moment.studywithme.ui.screens.home.pomodoro.TimerState

data class BreakUiState(
    val timer: String,
    val timerState: TimerState,
    val userPreferences: UserPreferences
)
