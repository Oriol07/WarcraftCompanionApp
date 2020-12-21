package com.oriolcomas.warcraft.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oriolcomas.warcraft.model.News
import com.oriolcomas.warcraft.network.Callback
import com.oriolcomas.warcraft.network.FirestoreService
import java.lang.Exception

class NewsViewModel : ViewModel(){

    val firestoreService = FirestoreService()
    var listNews: MutableLiveData<List<News>> = MutableLiveData()// llista dels nostres posts. Fusio de view model amb live data
    //view model: permet que les dades sovreviveixin als canvis de configuració
    //LiveData : actualitza la informació de l'Ui instantaneament
    var isLoading = MutableLiveData<Boolean>()

    fun refresh() {
        getNewsFromFirebase()
    }

    private fun getNewsFromFirebase() {
        firestoreService.getNews(object: Callback<List<News>> {
            override fun onSuccess(result: List<News>?) {
                listNews.postValue(result)
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