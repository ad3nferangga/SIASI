package com.apps.absensi.view.absen

import android.Manifest
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.apps.absensi.R
import com.apps.absensi.databinding.ActivityAbsenBinding
import com.apps.absensi.utils.BitmapManager.bitmapToBase64
import com.apps.absensi.utils.SessionLogin
import com.apps.absensi.viewmodel.AbsenViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AbsenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAbsenBinding
    private lateinit var session: SessionLogin
    private lateinit var progressDialog: ProgressDialog
    private lateinit var absenViewModel: AbsenViewModel
    private var strCurrentLatitude = 0.0
    private var strCurrentLongitude = 0.0
    private lateinit var strBase64Photo: String
    private lateinit var strCurrentLocation: String
    private lateinit var strTitle: String
    private lateinit var strStatus: String

    private val cameraPermissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) capturePhoto() else Toast.makeText(this, "Izin Kamera diperlukan", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbsenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionLogin(applicationContext)
        setInitLayout()
        setCurrentLocation()
        setUploadData()
    }

    private fun setCurrentLocation() {
        progressDialog.show()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            setDummyLocation()
            progressDialog.dismiss()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this) { location ->
                progressDialog.dismiss()
                if (location != null) {
                    strCurrentLatitude = location.latitude
                    strCurrentLongitude = location.longitude
                    val geocoder = Geocoder(this@AbsenActivity, Locale.getDefault())
                    try {
                        val addressList = geocoder.getFromLocation(strCurrentLatitude, strCurrentLongitude, 1)
                        if (addressList != null && addressList.isNotEmpty()) {
                            strCurrentLocation = addressList[0].getAddressLine(0)
                            binding.inputLokasi.setText(strCurrentLocation)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    setDummyLocation()
                    Toast.makeText(this@AbsenActivity, "Gagal mendapatkan lokasi. Menggunakan lokasi dummy.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun setDummyLocation() {
        strCurrentLatitude = -6.200000
        strCurrentLongitude = 106.816666
        strCurrentLocation = "Lokasi dummy (Jakarta, Indonesia)"
        binding.inputLokasi.setText(strCurrentLocation)
    }

    private fun setInitLayout() {
        progressDialog = ProgressDialog(this)
        strTitle = intent.extras?.getString(DATA_TITLE).toString()
        strStatus = intent.extras?.getString(DATA_STATUS).toString()
        if (session.isLoggedIn()) {
            val namaUser = session.pref.getString(SessionLogin.KEY_NAMA, "")
            binding.inputNama.setText(namaUser)
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        absenViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(AbsenViewModel::class.java)

        binding.inputTanggal.setOnClickListener {
            val tanggalAbsen = Calendar.getInstance()
            val date = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                tanggalAbsen[Calendar.YEAR] = year
                tanggalAbsen[Calendar.MONTH] = monthOfYear
                tanggalAbsen[Calendar.DAY_OF_MONTH] = dayOfMonth
                val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
                binding.inputTanggal.setText(simpleDateFormat.format(tanggalAbsen.time))
            }
            DatePickerDialog(this@AbsenActivity, date, tanggalAbsen[Calendar.YEAR], tanggalAbsen[Calendar.MONTH], tanggalAbsen[Calendar.DAY_OF_MONTH]).show()
        }

        binding.layoutImage.setOnClickListener {
            cameraPermissionRequest.launch(Manifest.permission.CAMERA)
        }
    }

    private fun capturePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResultLauncher.launch(intent)
    }

    private val cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val bitmap = result.data?.extras?.get("data") as Bitmap
            displayAndConvertImage(bitmap)
        }
    }

    private fun displayAndConvertImage(bitmap: Bitmap) {
        Glide.with(this).load(bitmap).into(binding.imageSelfie)
        strBase64Photo = bitmapToBase64(bitmap)
    }

    private fun setUploadData() {
        binding.btnAbsen.setOnClickListener {
            val strNama = binding.inputNama.text.toString()
            val strTanggal = binding.inputTanggal.text.toString()
            val strKeterangan = binding.inputKeterangan.text.toString()
            val userId = session.getUserId() ?: ""
            if (strBase64Photo.isEmpty() || strNama.isEmpty() || strCurrentLocation.isEmpty() || strTanggal.isEmpty() || strKeterangan.isEmpty()) {
                Toast.makeText(this@AbsenActivity, "Data tidak boleh ada yang kosong!", Toast.LENGTH_SHORT).show()
            } else {
                absenViewModel.addDataAbsen(userId, strBase64Photo, strNama, strTanggal, strCurrentLocation, strKeterangan, strStatus)
                Toast.makeText(this@AbsenActivity, "Laporan Anda terkirim, tunggu info selanjutnya ya!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DATA_TITLE = "TITLE"
        const val DATA_STATUS = "STATUS"
    }
}
