package com.apps.absensi.view.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.apps.absensi.R
import com.apps.absensi.databinding.ActivityLoginBinding
import com.apps.absensi.utils.SessionLogin
import com.apps.absensi.view.main.MainActivity
import com.apps.absensi.view.register.RegisterActivity
import com.apps.absensi.viewmodel.LoginViewModel
import com.apps.absensi.model.UserModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: SessionLogin
    private lateinit var loginViewModel: LoginViewModel

    private val REQ_PERMISSION = 101
    private lateinit var strNama: String
    private lateinit var strPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel
        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(LoginViewModel::class.java)

        setPermission()
        setInitLayout()

        // Set listener untuk link ke RegisterActivity
        binding.tvRegisterLink.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQ_PERMISSION
            )
        }
    }

    private fun setInitLayout() {
        session = SessionLogin(applicationContext)

        if (session.isLoggedIn()) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            strNama = binding.inputNama.text.toString().trim()
            strPassword = binding.inputPassword.text.toString().trim()

            if (strNama.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(
                    this@LoginActivity, "Form tidak boleh kosong!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                // Cek kredensial user di database
                loginViewModel.checkUserCredentials(strNama, strPassword)
                    .subscribe({ user ->
                        if (user != null) {
                            // Login berhasil, ambil userId dari user model
                            val userId = user.userId
                            session.createLoginSession(strNama, userId)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Jika username atau password salah
                            Toast.makeText(
                                this@LoginActivity, "Username atau password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, { error ->
                        Toast.makeText(
                            this@LoginActivity, "Terjadi kesalahan: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    })
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                val intent = intent
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
