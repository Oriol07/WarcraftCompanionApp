package com.oriolcomas.warcraft.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.login.LoginManager
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.view.activities.LoginActivity
import com.oriolcomas.warcraft.view.adapter.SearchAdapter
import com.oriolcomas.warcraft.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(){

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var viewModel: SearchViewModel

    private lateinit var keyWord: String
    private lateinit var etSearch: EditText
    private lateinit var ibSearch: ImageButton


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_search, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)



        searchAdapter = SearchAdapter()

        rvSearchUsername.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = searchAdapter
        }
        observeViewModel()

        initViews(view)
        viewModel.refresh(etSearch.text.toString())
        initListeners(view)

    }

    fun initViews(itemView: View)
    {
        etSearch = itemView.findViewById<EditText>(R.id.etSearch)
        ibSearch = itemView.findViewById<ImageButton>(R.id.ibSearch)
    }

    private fun initListeners(view: View) {

        ibSearch.setOnClickListener {
            Firebase.analytics.logEvent("SearchButtonClick", null)
            viewModel.refresh(etSearch.text.toString())
            Log.i("SearchFragment", "Username: ${etSearch.text.toString()}")
        }
    }

    fun observeViewModel()
    {
        viewModel.listUsers.observe(this, Observer<List<User>>{ post -> searchAdapter.updateData(post)})
        viewModel.isLoading.observe(this, Observer<Boolean> {
            if (it != null)
                rlBaseSearch.visibility = View.INVISIBLE
        })
    }

}