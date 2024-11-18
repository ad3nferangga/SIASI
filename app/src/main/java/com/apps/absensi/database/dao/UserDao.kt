package com.apps.absensi.database.dao

import com.apps.absensi.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Single

class UserDao {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("tb_users")

    fun getAllUsers(): Single<List<UserModel>> {
        return Single.create { emitter ->
            collection.get()
                .addOnSuccessListener { result ->
                    val list = result.toObjects(UserModel::class.java)
                    emitter.onSuccess(list) // Emit list of users
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception) // Emit error if retrieval fails
                }
        }
    }

    fun insertUser(userModel: UserModel): Single<String> {
        return Single.create { emitter ->
            collection.add(userModel)
                .addOnSuccessListener { documentReference ->
                    emitter.onSuccess(documentReference.id) // Emit the new document ID
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception) // Emit error if insertion fails
                }
        }
    }

    fun deleteUserById(userId: String): Single<Boolean> {
        return Single.create { emitter ->
            collection.whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        for (document in result) {
                            collection.document(document.id).delete()
                        }
                        emitter.onSuccess(true) // Emit success if deletion succeeded
                    } else {
                        emitter.onSuccess(false) // Emit false if no documents were found
                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception) // Emit error if the request fails
                }
        }
    }

    fun deleteAllUsers(): Single<Boolean> {
        return Single.create { emitter ->
            collection.get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        collection.document(document.id).delete()
                    }
                    emitter.onSuccess(true) // Emit success after deleting all documents
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception) // Emit error if deletion fails
                }
        }
    }

    fun getUserByUsernameAndPassword(username: String, password: String): Single<UserModel> {
        return Single.create { emitter ->
            collection
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener { result ->
                    val user = result.toObjects(UserModel::class.java).firstOrNull()
                    if (user != null) {
                        emitter.onSuccess(user)  // Emit user if found
                    } else {
                        emitter.onError(NoSuchElementException("User not found")) // Emit error if not found
                    }
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)  // Emit error if request fails
                }
        }
    }

}
