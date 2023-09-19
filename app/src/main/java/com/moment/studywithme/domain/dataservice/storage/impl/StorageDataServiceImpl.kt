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
        return if(checkIfUserPreferencesExist()){
            println("exist")
            db.userPreferencesDao().getUserPreferences()
        } else {
            println("not exist")
            createUserPreferences(userPreferences = UserPreferences())
            return UserPreferences()
        }
    }

    override fun createUserPreferences(userPreferences: UserPreferences) {
        db.userPreferencesDao().create(userPreferences)
    }

    override fun updateUserPreferences(userPreferences: UserPreferences) {
        println("update user pref ${userPreferences.uid}")
        db.userPreferencesDao().update(userPreferences)
    }

    override fun checkIfUserPreferencesExist(): Boolean {
        return db.userPreferencesDao().checkIfUserPreferencesExist()
    }
}
