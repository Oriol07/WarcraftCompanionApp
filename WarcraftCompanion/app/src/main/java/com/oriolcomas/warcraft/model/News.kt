package com.oriolcomas.warcraft.model

import java.io.Serializable
import java.util.*

class News : Serializable {
    var title = ""
    var text = ""
    var image = ""
    lateinit var date: Date
}