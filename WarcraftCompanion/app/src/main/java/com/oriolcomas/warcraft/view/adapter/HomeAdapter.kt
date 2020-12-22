package com.oriolcomas.warcraft.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.network.FirestoreService
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.ViewHolder>(){

    var listPosts = ArrayList<Post>()

    val firestoreService = FirestoreService()

    //Crear o decir cual sera el diseño usado para nuestras listas // En aquest cas volem el disseny del layout item_post.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))

    //Quants elements tenim
    override fun getItemCount() = listPosts.size

    //Les dades que anem a carregar
    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        val post = listPosts[position]
       // val user

        holder.tvTitle.text = post.title
        firestoreService.getUser(post.userId)
        {
            user: User? ->
            holder.tvUsername.text= user?.username
            Glide.with(holder.itemView.context)
                .load(user?.avatar)
                .apply(RequestOptions.circleCropTransform()) // posar forma circular
                .into(holder.ivUserAvatar)

        }

        val cal = Calendar.getInstance()
        cal.time = post.date

        holder.tvDatePost.text = getDateTime(post.date)



        Glide.with(holder.itemView.context)
            .load(post.image)
            .into(holder.ivImagePost)



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
        var tvUsername = itemView.findViewById<TextView>(R.id.tvPostUsername)
        var ivImagePost = itemView.findViewById<ImageView>(R.id.ivPostImage)
        var ivUserAvatar = itemView.findViewById<ImageView>(R.id.ivPostUserProfile)
        var tvDatePost = itemView.findViewById<TextView>(R.id.tvDatePost)


    }

    private fun getDateTime(date: Date): String? {
        val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
        val netDate = date
        val date = sdf.format(netDate)
        return date.toString()
    }

}