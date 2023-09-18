package com.moment.studywithme.domain.dataservice.storage.impl

import com.moment.studywithme.domain.SWMDatabase
import com.moment.studywithme.domain.dataservice.storage.StorageDataService
import com.moment.studywithme.domain.model.Task
import com.moment.studywithme.domain.model.UserPreferences

class StorageDataServiceImpl(private val db: SWMDatabase) : StorageDataService {
    override fun getAllTasks(): List<Task> {
        return db.taskDao().getAll()
    }

    override fun createTask(task: Task) {
        db.taskDao().insert(task)
    }

    override fun deleteTask(task: Task) {
        db.taskDao().delete(task)
    }

    override fun updateTask(task: Task) {
        db.taskDao().update(task)
    }

    override fun getUserPreferences(): UserPreferences {
        var userPref = db.userPreferencesDao().getUserPreferences()
        if (userPref == null) {
            userPref = UserPreferences()
            createUserPreferences(userPreferences = userPref)
        }
        return userPref
    }

    override fun createUserPreferences(userPreferences: UserPreferences) {
        db.userPreferencesDao().create(userPreferences)
    }

    override fun updateUserPreferences(userPreferences: UserPreferences) {
        db.userPreferencesDao().update(userPreferences)
    }
}
