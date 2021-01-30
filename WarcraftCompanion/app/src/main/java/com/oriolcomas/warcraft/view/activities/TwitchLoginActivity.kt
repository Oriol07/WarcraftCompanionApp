package com.oriolcomas.warcraft.view.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.Constants
import com.oriolcomas.warcraft.model.OAuthTokensResponse
import com.oriolcomas.warcraft.network.NetworkManager
import com.oriolcomas.warcraft.network.UserManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class TwitchLoginActivity : AppCompatActivity() {

    private val TAG = "TwitchLoginActivity"
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
            .build()

        webView.settings.javaScriptEnabled = true;
        webView.webViewClient  = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                if (request?.url?.toString()?.startsWith(Constants.OAUTH_REDIRECT_URI) == true)
                {
                    //Login succes!
                    Log.i(TAG, "Login succes with URL: ${request?.url}")
                    request.url.getQueryParameter("code")?.let{
                        Log.i(TAG, "Got authentication CODE $it")
                        webView.visibility = View.GONE


                        //Exchange code -> access token

                        getAccessTokens(it)

                    }  ?: run {
                        // Handle error
                        Log.i(TAG, "ERROR twitch")

                    }


                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        Log.i(TAG, uri.toString())

        //load url
        webView.loadUrl(uri.toString())



    }

    private fun getAccessTokens(authorizationCode: String)
    {

        Log.i(TAG, "Getting access tokens with authorizationCode $authorizationCode")

        val httpClient = NetworkManager.createHttpClient()

        lifecycleScope.launch {
            Log.i(TAG, "Launching get Tokens request")

            //Change to Background thread
            withContext(Dispatchers.IO) {
                try {
                    val response =
                        httpClient.post<OAuthTokensResponse>("https://id.twitch.tv/oauth2/token")
                        {
                            parameter("client_id", Constants.OAUTH_CLIENT_ID)
                            parameter("client_secret", Constants.OAUTH_CLIENT_SECRET)
                            parameter("code", authorizationCode)
                            parameter("grant_type", "authorization_code")
                            parameter("redirect_uri", Constants.OAUTH_REDIRECT_URI)
                        }
                    Log.i(TAG, "Got response from Twitch: $response")
                    UserManager(this@TwitchLoginActivity)
                        .saveAccessToken(response.accessToken)

                    finish()
                }catch(t: Throwable)
                {
                    Log.e(TAG, "error Coroutine")
                }

            }

        }
    }
}