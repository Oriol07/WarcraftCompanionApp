package com.oriolcomas.warcraft.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.network.Callback
import com.oriolcomas.warcraft.network.FirestoreService
import java.lang.Exception


class HomeViewModel : ViewModel(){

    val firestoreService = FirestoreService()
    var listPosts: MutableLiveData<List<Post>> = MutableLiveData()// llista dels nostres posts. Fusio de view model amb live data
    //view model: permet que les dades sovreviveixin als canvis de configuració
    //LiveData : actualitza la informació de l'Ui instantaneament
    var isLoading = MutableLiveData<Boolean>()

    fun refresh() {
        getPostsFromFirebase()
    }

    private fun getPostsFromFirebase() {
        firestoreService.getPosts(object: Callback<List<Post>> {
            override fun onSuccess(result: List<Post>?) {
                listPosts.postValue(result)
                processFinished()
            }

            override fun onFailed(exception: Exception) {
                processFinished()
            }

        })
    }

    fun processFinished()
    {
        isLoading.value = true

    }

}