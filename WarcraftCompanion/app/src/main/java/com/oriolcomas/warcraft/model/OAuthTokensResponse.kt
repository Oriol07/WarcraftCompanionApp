package com.oriolcomas.warcraft.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OAuthTokensResponse (
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String?,
    @SerialName("expires_in") val expiresIn: Int?,
    val scope: List<String>?,
    @SerialName("token_type") val tokenType: String?
)