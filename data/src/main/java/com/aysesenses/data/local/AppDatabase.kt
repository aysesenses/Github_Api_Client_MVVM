package com.aysesenses.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aysesenses.data.local.dao.AppDao
import com.aysesenses.data.local.entitiy.UserDetailsEntity
import com.aysesenses.data.local.entitiy.UserEntity

@Database(entities = [UserEntity::class, UserDetailsEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getDAO(): AppDao

    companion object {
        private var dbINSTANCE: AppDatabase? = null

        fun getAppDB(context: Context): AppDatabase {
            if(dbINSTANCE == null) {
                dbINSTANCE = Room.databaseBuilder<AppDatabase>(
                    context.applicationContext, AppDatabase::class.java, "MYDB"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return dbINSTANCE!!
        }
    }
}