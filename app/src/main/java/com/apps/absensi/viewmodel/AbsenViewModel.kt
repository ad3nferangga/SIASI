package com.apps.absensi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.apps.absensi.database.DatabaseClient
import com.apps.absensi.database.dao.DatabaseDao
import com.apps.absensi.model.ModelDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class AbsenViewModel(application: Application) : AndroidViewModel(application) {

    private val databaseDao: DatabaseDao? = DatabaseClient.getInstance(application.applicationContext).appDatabase.databaseDao

    fun addDataAbsen(
        userId: String,
        foto: String,
        nama: String,
        tanggal: String,
        lokasi: String,
        keterangan: String,
        status: String
    ) {
        Completable.fromAction {
            val modelDatabase = ModelDatabase().apply {
                this.userId = userId
                this.fotoSelfie = foto
                this.nama = nama
                this.tanggal = tanggal
                this.lokasi = lokasi
                this.keterangan = keterangan
                this.status = status
            }
            databaseDao?.insertData(modelDatabase)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}
