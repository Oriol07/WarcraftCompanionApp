package com.oriolcomas.warcraft.network

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.model.User


const val POSTS_COLLECTION_NAME = "post"
const val USERS_COLLECTION_NAME = "users"

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
    fun getUserPosts(callback: Callback<List<Post>>, userID: String)
    {
        firebaseFirestore
            .collection(USERS_COLLECTION_NAME)
            .whereEqualTo("userId", userID)
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

    fun setNewUser( user: User)
    {

        firebaseFirestore
            .collection(USERS_COLLECTION_NAME)
            .document(user.userId)
            .set(user)
            .addOnCompleteListener {
                if ( it.isSuccessful)
                {
                    Log.i("RegisterActivity", "User profile created")
                }
                else
                {
                    Log.i("RegisterActivity", "User profile not created")
                }

            }
    }

    private fun finish() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}