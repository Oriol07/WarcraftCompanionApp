package com.oriolcomas.warcraft.network

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.oriolcomas.warcraft.model.News
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.model.User


const val POSTS_COLLECTION_NAME = "post"
const val USERS_COLLECTION_NAME = "users"
const val NEWS_COLLECTION_NAME = "news"

class FirestoreService  {

    val firebaseFirestore = FirebaseFirestore.getInstance() //Conexio directa cap a la base de dades
    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build() // configuracio, obtenim el mode offline

    init {
        firebaseFirestore.firestoreSettings = settings //dades descargades persisteixen si estem offline
    }

    fun getPosts(callback: Callback<List<Post>>)
    {
        firebaseFirestore.collection(POSTS_COLLECTION_NAME)
            .orderBy("date", Query.Direction.DESCENDING)
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

    fun getNews(callback: Callback<List<News>>)
    {
        firebaseFirestore.collection(NEWS_COLLECTION_NAME)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result)
                {
                    val list = result.toObjects(News::class.java)
                    callback.onSuccess(list)
                    break
                }
            }
    }

    fun getUserPosts(callback: Callback<List<Post>>)
    {
        firebaseFirestore
            .collection(POSTS_COLLECTION_NAME)
            .whereEqualTo("userId", getCurrentUserId())
            .orderBy("date", Query.Direction.DESCENDING)
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

    fun getUser(userId: String, resultListener: ((User?)) ->Unit)
    {
       firebaseFirestore
           .collection(USERS_COLLECTION_NAME)
           .document(userId)
           .get()
           .addOnSuccessListener {result ->
                if (result != null) {
                     val user = result.toObject(User::class.java)
                    Log.d("GetUser", "DocumentSnapshot user: ${user!!.username}") //Donà bé! p
                    resultListener(user)
                } else {
                    Log.d("GetUser", "No such document")
                    resultListener(null)
                }
            }
                .addOnFailureListener { exception ->
                    Log.d("GetUser", "get failed with ", exception)
                    resultListener(null)
                }
    }

    fun setNewPost(post: Post, callback: Callback<Post>) {
        firebaseFirestore.collection(POSTS_COLLECTION_NAME).document().set(post)
            .addOnSuccessListener {
                Log.d("SetNewPost", "Post successfully created!")
                callback.onSuccess(post)
            }
            .addOnFailureListener {
                    e -> Log.w("SetNewPost", "Error creating document", e)
                    callback.onFailed(e);
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

    fun getCurrentUserId() : String{

        val user = Firebase.auth.currentUser
        user?.let {

             val uid = user.uid;

            return uid
        }
        return ""
    }


}