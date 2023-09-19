package com.moment.studywithme.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.moment.studywithme.domain.model.UserPreferences

@Dao
interface UserPreferencesDao {

    @Query("SELECT EXISTS(SELECT * FROM UserPreferences)")
    fun checkIfUserPreferencesExist(): Boolean

    @Query("SELECT * FROM UserPreferences LIMIT 1")
    fun getUserPreferences(): UserPreferences

    @Insert
    fun create(vararg userPreferences: UserPreferences)

    @Update
    fun update(userPreferences: UserPreferences)
}