package com.apps.absensi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.apps.absensi.database.dao.UserDao
import com.apps.absensi.model.UserModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.UUID

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = UserDao()

    fun addUserData(
        fullName: String,
        username: String,
        password: String,
        email: String,
        phone: String,
        onSuccess: () -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        val userId = UUID.randomUUID().toString()
        val user = UserModel().apply {
            this.userId = userId
            this.fullName = fullName
            this.username = username
            this.password = password
            this.email = email
            this.phone = phone
        }
        userDao.insertUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { error -> onFailure(error) }
            )
    }
}
