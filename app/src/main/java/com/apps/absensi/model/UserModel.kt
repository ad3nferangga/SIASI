package com.apps.absensi.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.io.Serializable

@Entity(tableName = "tb_users")
class UserModel : Serializable {

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    lateinit var userId: String  // Menggunakan String untuk user_id

    @ColumnInfo(name = "full_name")
    lateinit var fullName: String

    @ColumnInfo(name = "username")
    lateinit var username: String

    @ColumnInfo(name = "password")
    lateinit var password: String

    @ColumnInfo(name = "email")
    lateinit var email: String

    @ColumnInfo(name = "phone")
    lateinit var phone: String
}
