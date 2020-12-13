package com.oriolcomas.warcraft.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Post

class ProfileAdapter(val profileListener: ProfileListener) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>(){
    var listPosts = ArrayList<Post>()

    //Crear o decir cual sera el diseño usado para nuestras listas // En aquest cas volem el disseny del layout item_post.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_profile_post, parent, false))

    //Quants elements tenim
    override fun getItemCount() = listPosts.size

    //Les dades que anem a carregar
    override fun onBindViewHolder(holder: ProfileAdapter.ViewHolder, position: Int) {
        val post = listPosts[position]

        holder.tvTitle.text = post.title

        Glide.with(holder.itemView.context)
            .load(post.image)
            //.apply(RequestOptions.circleCropTransform()) //Aixó per la foto del Usuari no d'aquest
            .into(holder.ivImagePost)

        holder.itemView.setOnClickListener{
            profileListener.onHomeClicked(post, position)
        }

    }

    fun updateData(data: List<Post>)
    {
        listPosts.clear()
        listPosts.addAll(data)
        notifyDataSetChanged()
    }

    //per enllaç a a cada un dels elements visuals
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitle = itemView.findViewById<TextView>(R.id.tvPostTitle)
        var ivImagePost = itemView.findViewById<ImageView>(R.id.ivPostImage)
        //  var tvViewComments = itemView.findViewById<ImageView>(R.id.tvPostViewComments)

    }



}