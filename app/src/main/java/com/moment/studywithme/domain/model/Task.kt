package com.moment.studywithme.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey val tid: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "is_done") var isDone: Boolean?
)