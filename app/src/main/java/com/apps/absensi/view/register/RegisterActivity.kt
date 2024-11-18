package com.apps.absensi.view.register

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.apps.absensi.databinding.ActivityRegisterBinding
import com.apps.absensi.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(RegisterViewModel::class.java)

        binding.btnRegister.setOnClickListener {
            val nama = binding.inputNama.text.toString().trim()
            val username = binding.inputUsername.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val phone = binding.inputPhone.text.toString().trim()

            if (nama.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                registerViewModel.addUserData(
                    fullName = nama,
                    username = username,
                    password = password,
                    email = email,
                    phone = phone,
                    onSuccess = {
                        Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onFailure = { error ->
                        Toast.makeText(this, "Registrasi gagal: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
