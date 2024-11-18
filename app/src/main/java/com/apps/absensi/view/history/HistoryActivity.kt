package com.apps.absensi.view.history

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.apps.absensi.R
import com.apps.absensi.databinding.ActivityHistoryBinding
import com.apps.absensi.model.ModelDatabase
import com.apps.absensi.utils.SessionLogin
import com.apps.absensi.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity(), HistoryAdapter.HistoryAdapterCallback {

    private lateinit var binding: ActivityHistoryBinding
    private var modelDatabaseList: MutableList<ModelDatabase> = ArrayList()
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var sessionLogin: SessionLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionLogin = SessionLogin(this)
        setInitLayout()
        setViewModel()
    }

    private fun setInitLayout() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        binding.tvNotFound.visibility = View.GONE
        historyAdapter = HistoryAdapter(this, modelDatabaseList, this)
        binding.rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            adapter = historyAdapter
        }
    }

    private fun setViewModel() {
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        historyViewModel.dataLaporan.observe(this) { modelDatabases: List<ModelDatabase> ->
            val userId = sessionLogin.getUserId()
            if (userId != null) {
                historyAdapter.setDataAdapter(modelDatabases)
                historyAdapter.filterDataByUserId(userId)
                if (historyAdapter.itemCount == 0) {
                    binding.tvNotFound.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.GONE
                } else {
                    binding.tvNotFound.visibility = View.GONE
                    binding.rvHistory.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDelete(modelDatabase: ModelDatabase?) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Hapus riwayat ini?")
        alertDialogBuilder.setPositiveButton("Ya, Hapus") { dialogInterface, i ->
            modelDatabase?.let {
                historyViewModel.deleteDataById(it.userId)
                Toast.makeText(this@HistoryActivity, "Yeay! Data yang dipilih sudah dihapus", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialogBuilder.setNegativeButton("Batal") { dialogInterface: DialogInterface, i: Int ->
            dialogInterface.cancel()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
