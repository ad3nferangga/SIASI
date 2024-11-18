package com.apps.absensi.view.history

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apps.absensi.R
import com.apps.absensi.databinding.ListHistoryAbsenBinding
import com.apps.absensi.model.ModelDatabase
import com.apps.absensi.utils.BitmapManager.base64ToBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class HistoryAdapter(
    var mContext: Context,
    var modelDatabase: MutableList<ModelDatabase>,
    var mAdapterCallback: HistoryAdapterCallback
) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    fun setDataAdapter(items: List<ModelDatabase>) {
        modelDatabase.clear()
        modelDatabase.addAll(items)
        notifyDataSetChanged()
    }

    fun filterDataByUserId(userId: String) {
        val filteredList = modelDatabase.filter { it.userId == userId }
        modelDatabase.clear()
        modelDatabase.addAll(filteredList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListHistoryAbsenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = modelDatabase[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return modelDatabase.size
    }

    inner class ViewHolder(private val binding: ListHistoryAbsenBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ModelDatabase) {
            binding.tvNomor.text = data.userId
            binding.tvNama.text = data.nama
            binding.tvLokasi.text = data.lokasi
            binding.tvAbsenTime.text = data.tanggal
            binding.tvStatusAbsen.text = data.keterangan

            Glide.with(mContext)
                .load(base64ToBitmap(data.fotoSelfie))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_photo_camera)
                .into(binding.imageProfile)

            when (data.status) {
                "Masuk" -> {
                    binding.colorStatus.setBackgroundResource(R.drawable.bg_circle_radius)
                    binding.colorStatus.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
                }
                "Keluar" -> {
                    binding.colorStatus.setBackgroundResource(R.drawable.bg_circle_radius)
                    binding.colorStatus.backgroundTintList = ColorStateList.valueOf(Color.RED)
                }
                "Izin" -> {
                    binding.colorStatus.setBackgroundResource(R.drawable.bg_circle_radius)
                    binding.colorStatus.backgroundTintList = ColorStateList.valueOf(Color.BLUE)
                }
            }

            binding.cvHistory.setOnClickListener {
                mAdapterCallback.onDelete(data)
            }
        }
    }

    interface HistoryAdapterCallback {
        fun onDelete(modelDatabase: ModelDatabase?)
    }
}
