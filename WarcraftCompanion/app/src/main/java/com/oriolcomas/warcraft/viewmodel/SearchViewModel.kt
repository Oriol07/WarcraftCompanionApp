package com.oriolcomas.warcraft.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.network.Callback
import com.oriolcomas.warcraft.network.FirestoreService
import java.lang.Exception


class SearchViewModel : ViewModel() {
    val firestoreService = FirestoreService()
    var listUsers: MutableLiveData<List<User>> = MutableLiveData()

    var isLoading = MutableLiveData<Boolean>()

    fun refresh(s: String) {
        getUsersFromFirebase(s)
    }

    private fun getUsersFromFirebase(s: String) {
        firestoreService.getUsersWithString(s, object: Callback<List<User>> {
            override fun onSuccess(result: List<User>?) {
                listUsers.postValue(result)
                if (result != null) {
                    result.forEach {
                        Log.i("Search", "Username = ${it.username}")
                    }
                }
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