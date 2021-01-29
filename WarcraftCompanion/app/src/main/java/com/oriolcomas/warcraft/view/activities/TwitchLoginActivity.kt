package com.oriolcomas.warcraft.view.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Constants

class TwitchLoginActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_twitch_login)
        initViews()
        loadOAuthUrl()

    }

    private fun initViews()
    {
        webView = findViewById<WebView>(R.id.wvTwitchLogin);

    }
    private fun loadOAuthUrl()
    {
       // prepare URL

        /*
        GET https://id.twitch.tv/oauth2/authorize?client_id=e7nu2rpirbkm1089lyk024seg2fo05&redirect_uri=http://localhost&response_type=code

        * */
        val uri = Uri.parse("https://id.twitch.tv/oauth2/authorize")
            .buildUpon()
            .appendQueryParameter("client_id", Constants.OAUTH_CLIENT_ID)
            .appendQueryParameter("redirect_uri", Constants.OAUTH_REDIRECT_URI)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", listOf("user:edit","user:read:email").joinToString(" "))

        webView.settings.javaScriptEnabled = true;

        //load url
        webView.loadUrl(uri.toString())



    }
}