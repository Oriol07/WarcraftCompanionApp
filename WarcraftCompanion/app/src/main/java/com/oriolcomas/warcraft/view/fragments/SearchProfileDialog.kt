package com.oriolcomas.warcraft.view.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.view.adapter.ProfileAdapter
import com.oriolcomas.warcraft.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_search_profile_dialog.*
import kotlinx.android.synthetic.main.fragment_search_profile_dialog.ivProfileUserAvatar
import kotlinx.android.synthetic.main.fragment_search_profile_dialog.rvProfilePost
import kotlinx.android.synthetic.main.fragment_search_profile_dialog.tvProfileUsername


class SearchProfileDialog : DialogFragment() {
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var viewModel: ProfileViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setStyle(STYLE_NORMAL,R.style.FullScreenDialogStyle)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_search_profile_dialog, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            toolbarProfile.navigationIcon =
                ContextCompat.getDrawable(view.context, R.drawable.ic_close)
            toolbarProfile.setTitleTextColor(Color.WHITE)
            toolbarProfile.setNavigationOnClickListener {
                dismiss()

            }

            val user = arguments?.getSerializable(("user")) as User
            tvProfileUsername.text = user.username

            Glide
                .with(this)
                .load(user.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfileUserAvatar)

            viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

            viewModel.setUserID(user.userId)

            viewModel.refresh()

            profileAdapter = ProfileAdapter()

            rvProfilePost.apply {
                layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                adapter = profileAdapter
            }
            observeViewModel()

        }

        override fun onStart() {
            super.onStart()
            dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }

    fun observeViewModel()
    {
        viewModel.listPosts.observe(this, Observer<List<Post>>{ post -> profileAdapter.updateData(post)})
        viewModel.isLoading.observe(this, Observer<Boolean> {
            if (it != null)
                rlBaseSearch.visibility = View.INVISIBLE
        })
    }

    }