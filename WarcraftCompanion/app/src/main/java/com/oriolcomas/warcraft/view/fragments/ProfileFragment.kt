package com.oriolcomas.warcraft.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.login.LoginManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.network.FirestoreService
import com.oriolcomas.warcraft.view.activities.LoginActivity
import com.oriolcomas.warcraft.view.activities.MainActivity
import com.oriolcomas.warcraft.view.adapter.ProfileAdapter
import com.oriolcomas.warcraft.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment(){
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var viewModel: ProfileViewModel

    private lateinit var ivProfileUserAvatar: ImageView
    private lateinit var ivProfileUsername: TextView
    private lateinit var ibExit: ImageButton
    val firestoreService = FirestoreService()




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_profile, container, false) }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        viewModel.refresh()

        profileAdapter = ProfileAdapter()

        rvProfilePost.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = profileAdapter
        }
        observeViewModel()

        initViews(view)
        initListeners(view)

    }

    private fun initListeners(view: View) {
        ibExit.setOnClickListener {
            Firebase.auth.signOut()
            LoginManager.getInstance().logOut();

            val intent = Intent(activity, LoginActivity::class.java)
            activity?.finish()
            startActivity(intent)
        }
    }

    fun observeViewModel()
    {
        viewModel.listPosts.observe(this, Observer<List<Post>>{ post -> profileAdapter.updateData(post)})
        viewModel.isLoading.observe(this, Observer<Boolean> {
            if (it != null)
                rlBaseProfile.visibility = View.INVISIBLE
        })
    }

    private fun initViews(itemView: View)
    {
        ivProfileUserAvatar = itemView.findViewById<ImageView>(R.id.ivProfileUserAvatar)
        ivProfileUsername = itemView.findViewById<TextView>(R.id.tvProfileUsername)
        ibExit = itemView.findViewById<ImageButton>(R.id.ibExit)

        firestoreService.getUser(firestoreService.getCurrentUserId())
        {
            user: User? ->
            ivProfileUsername.text = user?.username
            Glide.with(itemView.context)
                .load(user?.avatar)
                .apply(RequestOptions.circleCropTransform()) // posar forma circular
                .into(ivProfileUserAvatar)
        }
    }







}
