package com.apps.absensi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.apps.absensi.database.dao.UserDao
import com.apps.absensi.model.UserModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = UserDao()  // Inisialisasi UserDao

    fun checkUserCredentials(username: String, password: String): Single<UserModel> {
        return userDao.getUserByUsernameAndPassword(username, password)
            .subscribeOn(Schedulers.io()) // Operasi di thread background
            .observeOn(AndroidSchedulers.mainThread()) // Kembali ke UI thread
    }
}
