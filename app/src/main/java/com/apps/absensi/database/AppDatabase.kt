package com.apps.absensi.database

import com.apps.absensi.database.dao.DatabaseDao
import com.apps.absensi.database.dao.UserDao

class AppDatabase {

    val databaseDao = DatabaseDao()
    val userDao = UserDao()

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            if (instance == null) {
                instance = AppDatabase()
            }
            return instance!!
        }
    }
}
