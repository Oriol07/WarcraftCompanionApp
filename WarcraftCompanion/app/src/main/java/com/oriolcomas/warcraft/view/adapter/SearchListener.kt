package com.oriolcomas.warcraft.view.adapter

import com.oriolcomas.warcraft.model.User

interface UserListener {
    fun onHomeClicked(user: User, position: Int)
    {

    }
}