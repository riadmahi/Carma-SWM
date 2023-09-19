package com.moment.studywithme.domain.dataservice.storage

import com.moment.studywithme.domain.model.Task
import com.moment.studywithme.domain.model.UserPreferences

interface StorageDataService {
    fun getAllTasks(): List<Task>
    fun createTask(task: Task)
    fun deleteTask(task: Task)
    fun updateTask(task: Task)
    fun getUserPreferences(): UserPreferences
    fun createUserPreferences(userPreferences: UserPreferences)
    fun updateUserPreferences(userPreferences: UserPreferences)
    fun checkIfUserPreferencesExist(): Boolean
}