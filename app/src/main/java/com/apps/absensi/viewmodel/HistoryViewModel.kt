package com.apps.absensi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apps.absensi.database.DatabaseClient
import com.apps.absensi.database.dao.DatabaseDao
import com.apps.absensi.model.ModelDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    // Mendapatkan instance DatabaseDao dari DatabaseClient
    private val databaseDao: DatabaseDao? = DatabaseClient.getInstance(application.applicationContext).appDatabase.databaseDao

    // Inisialisasi data laporan dari metode getAllHistory di DatabaseDao
    var dataLaporan: LiveData<List<ModelDatabase>> = databaseDao?.getAllHistory() ?: MutableLiveData(emptyList())

    fun deleteDataById(uid: String) {
        Completable.fromAction {
            databaseDao?.deleteHistoryById(uid)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}
