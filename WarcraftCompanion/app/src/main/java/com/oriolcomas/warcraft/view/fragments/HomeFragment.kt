package com.oriolcomas.warcraft.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.view.adapter.HomeAdapter
import com.oriolcomas.warcraft.view.adapter.HomeListener
import com.oriolcomas.warcraft.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), HomeListener{

    private lateinit var homeAdapter: HomeAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_home, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        viewModel.refresh()

        homeAdapter = HomeAdapter(this)

        rvPost.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = homeAdapter
        }
        observeViewModel()

    }

    fun observeViewModel()
    {
        viewModel.listPosts.observe(this, Observer<List<Post>>{post -> homeAdapter.updateData(post)})
        viewModel.isLoading.observe(this, Observer<Boolean> {
            if (it != null)
               rlBasePost.visibility = View.INVISIBLE
        })
    }

}
