package com.oriolcomas.warcraft.model

import java.io.Serializable
import java.util.*

class Post : Serializable {
    var image: String = ""
    var title: String = ""
    var username: String = ""
    var userId: String = ""
    lateinit var date: Date


}

