package com.moment.studywithme.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserPreferences (
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "default_timer_in_ms") var defaultTimerInMs: Long = (25 * 60000).toLong(),
    @ColumnInfo(name = "current_timer_in_ms") val currentTimerInMs: Long = 0L,
    @ColumnInfo(name = "default_break_in_ms") var defaultBreakInMs: Long = (5 * 60000).toLong(),
    @ColumnInfo(name = "current_break_in_ms") val currentBreakInMs: Long = 0L,
    @ColumnInfo(name = "nb_rounds") var nbRounds: Int = 1,
)