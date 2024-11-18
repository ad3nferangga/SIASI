package com.apps.absensi.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.apps.absensi.R
import com.apps.absensi.databinding.ActivityMainBinding
import com.apps.absensi.utils.SessionLogin
import com.apps.absensi.view.absen.AbsenActivity
import com.apps.absensi.view.history.HistoryActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionLogin
    private lateinit var strTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setInitLayout()
    }

    private fun setInitLayout() {
        session = SessionLogin(this)
        session.checkLogin()

        // Membuka AbsenActivity dengan status "Masuk"
        binding.cvAbsenMasuk.setOnClickListener {
            strTitle = "Absen Masuk"
            val intent = Intent(this@MainActivity, AbsenActivity::class.java)
            intent.putExtra(AbsenActivity.DATA_TITLE, strTitle)
            intent.putExtra(AbsenActivity.DATA_STATUS, "Masuk")  // Tambahkan status "masuk"
            startActivity(intent)
        }

        // Membuka AbsenActivity dengan status "Keluar"
        binding.cvAbsenKeluar.setOnClickListener {
            strTitle = "Absen Keluar"
            val intent = Intent(this@MainActivity, AbsenActivity::class.java)
            intent.putExtra(AbsenActivity.DATA_TITLE, strTitle)
            intent.putExtra(AbsenActivity.DATA_STATUS, "Keluar")  // Tambahkan status "keluar"
            startActivity(intent)
        }

        // Membuka AbsenActivity dengan status "Izin"
        binding.cvPerizinan.setOnClickListener {
            strTitle = "Izin"
            val intent = Intent(this@MainActivity, AbsenActivity::class.java)
            intent.putExtra(AbsenActivity.DATA_TITLE, strTitle)
            intent.putExtra(AbsenActivity.DATA_STATUS, "Izin")  // Tambahkan status "izin"
            startActivity(intent)
        }

        // Membuka HistoryActivity
        binding.cvHistory.setOnClickListener {
            val intent = Intent(this@MainActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        // Tombol Logout
        binding.imageLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Yakin Anda ingin Logout?")
            builder.setCancelable(true)
            builder.setNegativeButton("Batal") { dialog, _ -> dialog.cancel() }
            builder.setPositiveButton("Ya") { _, _ ->
                session.logoutUser()
                finishAffinity()
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}
