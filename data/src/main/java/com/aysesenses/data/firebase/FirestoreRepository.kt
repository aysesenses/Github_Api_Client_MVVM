package com.aysesenses.data.firebase

import android.content.ContentValues
import android.util.Log
import com.aysesenses.data.local.entitiy.UserEntity
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirestoreRepository @Inject constructor(private val usersRef: CollectionReference) {
    fun saveSearchResults(mutableMap: MutableMap<String?, Any?>,term: String) {
        usersRef.document(term).set(mutableMap).addOnCompleteListener { task ->
            when (task.isSuccessful) {
                true ->  Log.d(ContentValues.TAG, "added:success")
                false ->  Log.d(ContentValues.TAG, "added:unsuccess")
            }
        }
    }
}