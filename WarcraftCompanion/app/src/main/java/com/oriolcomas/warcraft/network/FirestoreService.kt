package com.oriolcomas.warcraft.network

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.oriolcomas.warcraft.model.Post


const val POSTS_COLLECTION_NAME = "post"

class FirestoreService  {

    val firebaseFirestore = FirebaseFirestore.getInstance() //Conexio directa cap a la base de dades
    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build() // configuracio, obtenim el mode offline

    init {
        firebaseFirestore.firestoreSettings = settings //dades descargades persisteixen si estem offline
    }

    fun getPosts(callback: Callback<List<Post>>)
    {
        firebaseFirestore.collection(POSTS_COLLECTION_NAME)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result)
                {
                    val list = result.toObjects(Post::class.java)
                    callback.onSuccess(list)
                    break
                }
            }
    }




}