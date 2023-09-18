package com.moment.studywithme.di

import android.content.Context
import androidx.room.Room
import com.moment.studywithme.domain.SWMDatabase
import com.moment.studywithme.domain.dataservice.storage.impl.StorageDataServiceImpl

class SWMAppInjector(context: Context) {
    val db = Room.databaseBuilder(
        context,
        SWMDatabase::class.java, "database-swm"
    ).fallbackToDestructiveMigration().build()

    val storageDataService by lazy {
        StorageDataServiceImpl(db)
    }
}