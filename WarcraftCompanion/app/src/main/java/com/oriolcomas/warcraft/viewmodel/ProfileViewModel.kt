package com.oriolcomas.warcraft.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.network.Callback
import com.oriolcomas.warcraft.network.FirestoreService
import java.lang.Exception


class ProfileViewModel() : ViewModel() {
    val firestoreService = FirestoreService()
    var listPosts: MutableLiveData<List<Post>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()
    private lateinit var userId: String

    fun refresh() {
        getUserPostsFromFirebase()
    }

    private fun getUserPostsFromFirebase() {
        firestoreService.getUserPosts(userId, object: Callback<List<Post>> {
            override fun onSuccess(result: List<Post>?) {
                listPosts.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }
        })
    }

    fun setUserID(id: String)
    {
        userId = id
    }

    fun processFinished()
    {
        isLoading.value = true

    }

}