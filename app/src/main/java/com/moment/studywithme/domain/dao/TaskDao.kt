package com.moment.studywithme.domain.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.moment.studywithme.domain.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Insert
    fun insert(vararg task: Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task: Task)
}