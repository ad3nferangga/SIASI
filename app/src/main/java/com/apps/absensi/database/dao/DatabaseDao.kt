package com.apps.absensi.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apps.absensi.model.ModelDatabase
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseDao {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("tbl_absensi")

    fun getAllHistory(): LiveData<List<ModelDatabase>> {
        val liveData = MutableLiveData<List<ModelDatabase>>()
        collection.get().addOnSuccessListener { result ->
            val list = result.toObjects(ModelDatabase::class.java)
            liveData.value = list
        }
        return liveData
    }

    fun insertData(vararg modelDatabases: ModelDatabase) {
        for (model in modelDatabases) {
            collection.add(model)
        }
    }

    fun deleteHistoryById(uid: String) {
        collection.whereEqualTo("uid", uid).get().addOnSuccessListener { result ->
            for (document in result) {
                collection.document(document.id).delete()
            }
        }
    }

    fun deleteAllHistory() {
        collection.get().addOnSuccessListener { result ->
            for (document in result) {
                collection.document(document.id).delete()
            }
        }
    }
}
