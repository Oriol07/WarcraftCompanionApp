package com.oriolcomas.warcraft.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.GameResponse
import com.oriolcomas.warcraft.model.User

class StreamsAdapter : RecyclerView.Adapter<StreamsAdapter.ViewHolder>(){

    var listGames = ArrayList<GameResponse>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StreamsAdapter.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_top_games, parent, false)
    )

    //Quants elements tenim
    override fun getItemCount() = listGames.size

    //Les dades que anem a carregar
    override fun onBindViewHolder(holder: StreamsAdapter.ViewHolder, position: Int) {
        val game = listGames[position]
        // val user

        holder.tvGameTitle.text = game.name
        Log.i("StreamsFragment", game.name)

        Glide.with(holder.itemView.context)
            .load(game.box_art_url)
            .into(holder.ivGameImage)
    }

    fun updateData(data: List<GameResponse>)
    {
        listGames.clear()
        listGames.addAll(data)
        notifyDataSetChanged()
    }

    //per enllaç a a cada un dels elements visuals
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvGameTitle = itemView.findViewById<TextView>(R.id.tvGameTitle)
        var ivGameImage = itemView.findViewById<ImageView>(R.id.ivGameImage)
    }




}