package com.apps.absensi.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.apps.absensi.view.login.LoginActivity

class SessionLogin(var context: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0

    fun createLoginSession(nama: String, userId: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_NAMA, nama)
        editor.putString(KEY_USER_ID, userId)  // Menyimpan user_id di SharedPreferences
        editor.commit()
    }

    fun checkLogin() {
        if (!isLoggedIn()) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun isLoggedIn(): Boolean = pref.getBoolean(IS_LOGIN, false)

    // Fungsi untuk mendapatkan user_id yang tersimpan
    fun getUserId(): String? = pref.getString(KEY_USER_ID, null)

    companion object {
        private const val PREF_NAME = "AbsensiPref"
        private const val IS_LOGIN = "IsLoggedIn"
        const val KEY_NAMA = "NAMA"
        const val KEY_USER_ID = "USER_ID"
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}
