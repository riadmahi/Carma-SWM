package com.moment.studywithme.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.moment.studywithme.domain.model.UserPreferences

@Dao
interface UserPreferencesDao {

    @Query("SELECT * FROM UserPreferences WHERE uid = 0")
    fun getUserPreferences(): UserPreferences?

    @Insert
    fun create(vararg userPreferences: UserPreferences)

    @Update
    fun update(userPreferences: UserPreferences)
}