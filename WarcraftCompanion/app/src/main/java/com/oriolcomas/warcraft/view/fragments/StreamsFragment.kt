package com.oriolcomas.warcraft.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.view.activities.TwitchLoginActivity

class StreamsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? { return inflater.inflate(R.layout.fragment_streams, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnTwitchLogin).setOnClickListener{
            startActivity(Intent(requireActivity(), TwitchLoginActivity::class.java))
        }

    }


}