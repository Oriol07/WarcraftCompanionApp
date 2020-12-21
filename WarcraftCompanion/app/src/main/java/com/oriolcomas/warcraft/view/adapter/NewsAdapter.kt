package com.oriolcomas.warcraft.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.News
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter() : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    var listNews = ArrayList<News>()

    //Crear o decir cual sera el diseño usado para nuestras listas // En aquest cas volem el disseny del layout item_post.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false))

    //Quants elements tenim
    override fun getItemCount() = listNews.size

    //Les dades que anem a carregar
    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val news = listNews[position]
        // val user

        holder.tvTitleNews.text = news.title
        holder.tvTextNews.text = news.text
        holder.tvDateNews.text = getDateTime(news.date)





        Glide.with(holder.itemView.context)
            .load(news.image)
            .into(holder.ivImageNews)


    }

    fun updateData(data: List<News>)
    {
        listNews.clear()
        listNews.addAll(data)
        notifyDataSetChanged()
    }

    //per enllaç a a cada un dels elements visuals
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvTitleNews = itemView.findViewById<TextView>(R.id.tvNewsTitle)
        var tvTextNews = itemView.findViewById<TextView>(R.id.tvNewsText)
        var ivImageNews = itemView.findViewById<ImageView>(R.id.ivNewsImage)
        var tvDateNews = itemView.findViewById<TextView>(R.id.tvNewsDate)


    }

    private fun getDateTime(date: Date): String? {
        val sdf = SimpleDateFormat("dd/MM/yy hh:mm a")
        val netDate = date
        val date = sdf.format(netDate)
        return date.toString()
    }

}