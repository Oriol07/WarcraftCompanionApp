package com.oriolcomas.warcraft.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Constants
import com.oriolcomas.warcraft.model.GameResponse
import com.oriolcomas.warcraft.model.Post
import com.oriolcomas.warcraft.network.NetworkManager
import com.oriolcomas.warcraft.network.UserManager
import com.oriolcomas.warcraft.view.activities.TwitchLoginActivity
import com.oriolcomas.warcraft.view.adapter.SearchAdapter
import com.oriolcomas.warcraft.view.adapter.StreamsAdapter
import io.ktor.client.features.get
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.android.synthetic.main.fragment_streams.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StreamsFragment: Fragment() {

    private lateinit var twitchLoginButton: Button
    private lateinit var tvTwitchConnected: TextView
    private lateinit var streamsAdapter: StreamsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_streams, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeneres()
        getTopGames()

        streamsAdapter = StreamsAdapter()
        rvStreams.apply {
            layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
            adapter = streamsAdapter
        }


    }

    override fun onResume() {
        super.onResume()
        checkUserAvailability()

    }
    private fun initViews(view: View)
    {
        tvTwitchConnected = view.findViewById(R.id.tvTwitchConnected)
        twitchLoginButton = view.findViewById<Button>(R.id.btnTwitchLogin)
    }

    private fun initListeneres()
    {
        twitchLoginButton.setOnClickListener{
            startActivity(Intent(requireActivity(), TwitchLoginActivity::class.java))
        }
    }

    private fun checkUserAvailability()
    {
        val isLoggedIn = UserManager(
            requireContext()
        ).getAccessToken() != null
        if(isLoggedIn) {
            twitchLoginButton.visibility = View.GONE
            tvTwitchConnected.visibility = View.VISIBLE
        }
        else{
            tvTwitchConnected.visibility = View.GONE
            twitchLoginButton.visibility = View.VISIBLE
        }
    }

    private fun getTopGames()
    {
        val httpClient = NetworkManager.createHttpClient()
        viewLifecycleOwner.lifecycleScope.launch{
            withContext(Dispatchers.IO) {

                val accessToken = UserManager(requireContext()).getAccessToken()
                //Get Top Games
                try {
                    // 18122 wow
                    //https://api.twitch.tv/helix/games?id=18122
                    val response = httpClient.get<GameResponse>("https://api.twitch.tv/helix/games/top")
                    {
                        header("Client-Id", Constants.OAUTH_CLIENT_ID)
                        header("Authorization", "Bearer $accessToken")
                    }
                    Log.i("StreamsFragment", "Got Top games: $response")

                   // streamsAdapter.updateData(responses);
                    //val mutableList: MutableList<GameResponse> = mutableListOf(response)
                    withContext(Dispatchers.Main){




                        //Update UI

                       // streamsAdapter.updateData(mutableList);



                    }
                } catch (t: Throwable) {
                    Log.e("StreamsFragment", "Got Top games: ${t.message}")
                }
            }
        }
    }

}