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
import com.oriolcomas.warcraft.view.adapter.ProfileAdapter
import com.oriolcomas.warcraft.view.adapter.ProfileListener
import com.oriolcomas.warcraft.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(), ProfileListener {
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_profile, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        viewModel.refresh()

        profileAdapter = ProfileAdapter(this)

        rvProfilePost.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = profileAdapter
        }
        observeViewModel()

    }
    fun observeViewModel()
    {
        viewModel.listPosts.observe(this, Observer<List<Post>>{ post -> profileAdapter.updateData(post)})
        viewModel.isLoading.observe(this, Observer<Boolean> {
            if (it != null)
                rlBaseProfile.visibility = View.INVISIBLE
        })
    }





}
