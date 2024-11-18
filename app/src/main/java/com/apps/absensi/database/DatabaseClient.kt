package com.apps.absensi.database

import android.content.Context

class DatabaseClient private constructor() {

    val appDatabase: AppDatabase = AppDatabase.getInstance()

    companion object {
        private var instance: DatabaseClient? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): DatabaseClient {
            if (instance == null) {
                instance = DatabaseClient()
            }
            return instance!!
        }
    }
}
