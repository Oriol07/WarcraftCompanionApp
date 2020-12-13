package com.oriolcomas.warcraft.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.network.FirestoreService
import java.util.*


class AddPostFragment : Fragment() {

    lateinit var etImageLink: EditText
    lateinit var etTitlePost: EditText
    lateinit var btnAddPost: Button

    val firestoreService = FirestoreService()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_add_post, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       //initViews
        initViews(view);

        initListeners(view)

    }

    private fun initListeners(itemView: View) {

        btnAddPost.setOnClickListener{
            val title = etTitlePost.text.toString()
            //TODO: check if title is valid!

            val image = etImageLink.text.toString()
            //TODO: check if image is valid

            addPost(title, image)

        }
    }


    private fun initViews(itemView: View)
    {
        etTitlePost = itemView.findViewById<EditText>(R.id.etTitlePost)
        etImageLink = itemView.findViewById<EditText>(R.id.etImageLink)
        btnAddPost = itemView.findViewById<Button>(R.id.btnAddPost)


    }

    private fun addPost(title: String, image: String) {
        firestoreService.getUser(firestoreService.getCurrentUserId())
        {
            user: User? ->

            var newPost: Post = Post()
            newPost.image = image
            newPost.username = user!!.username
            newPost.userId = user!!.userId
            newPost.title = title
            newPost.date = Calendar.getInstance().time

            //New Post :)
            firestoreService.setNewPost(newPost)

        }
    }

}