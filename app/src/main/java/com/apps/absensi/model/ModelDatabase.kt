package com.apps.absensi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tbl_absensi")
class ModelDatabase : Serializable {

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    lateinit var userId: String  // Menggunakan String untuk user_id

    @ColumnInfo(name = "nama")
    lateinit var nama: String

    @ColumnInfo(name = "foto_selfie")
    lateinit var fotoSelfie: String

    @ColumnInfo(name = "tanggal")
    lateinit var tanggal: String

    @ColumnInfo(name = "lokasi")
    lateinit var lokasi: String

    @ColumnInfo(name = "keterangan")
    lateinit var keterangan: String

    @ColumnInfo(name = "status")
    lateinit var status: String
}
