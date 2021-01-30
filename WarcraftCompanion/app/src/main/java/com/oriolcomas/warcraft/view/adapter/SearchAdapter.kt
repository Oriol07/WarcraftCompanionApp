package com.oriolcomas.warcraft.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.network.FirestoreService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(val searchListener: SearchListener) : RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    var listUsers = ArrayList<User>()

    //Crear o decir cual sera el diseño usado para nuestras listas // En aquest cas volem el disseny del layout item_post.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false))

    //Quants elements tenim
    override fun getItemCount() = listUsers.size

    //Les dades que anem a carregar
    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val users = listUsers[position]
        // val user

        holder.tvSearchUsername.text = users.username

            Glide.with(holder.itemView.context)
                .load(users.avatar)
                .apply(RequestOptions.circleCropTransform()) // posar forma circular
                .into(holder.ivSearchUserProfile)
        holder.itemView.setOnClickListener{
            searchListener.onUserClicked(users, position)
        }

    }

    fun updateData(data: List<User>)
    {
        listUsers.clear()
        listUsers.addAll(data)
        notifyDataSetChanged()
    }

    //per enllaç a a cada un dels elements visuals
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvSearchUsername = itemView.findViewById<TextView>(R.id.tvSearchUsername)
        var ivSearchUserProfile = itemView.findViewById<ImageView>(R.id.ivSearchUserProfile)
    }





}