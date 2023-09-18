package com.moment.studywithme.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moment.studywithme.domain.model.Task
import com.moment.studywithme.domain.dao.TaskDao
import com.moment.studywithme.domain.dao.UserPreferencesDao
import com.moment.studywithme.domain.model.UserPreferences

@Database(entities = [Task::class, UserPreferences::class], version = 2)
abstract class SWMDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun userPreferencesDao(): UserPreferencesDao
}