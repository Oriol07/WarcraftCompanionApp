package com.oriolcomas.warcraft.view.adapter

import com.oriolcomas.warcraft.model.User

interface SearchListener {
    fun onUserClicked(user: User, position: Int)
    {

    }
}