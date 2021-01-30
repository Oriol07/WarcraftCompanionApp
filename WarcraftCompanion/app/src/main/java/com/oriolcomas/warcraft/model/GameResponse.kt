package com.oriolcomas.warcraft.model

import kotlinx.serialization.SerialName
import java.io.Serializable
import java.util.*
@kotlinx.serialization.Serializable

data class GameResponse(val id: String, val name: String, val box_art_url: String)
