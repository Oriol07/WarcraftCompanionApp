package com.oriolcomas.warcraft.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.network.FirestoreService
import java.util.*


class LoginActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private val callbackManager = CallbackManager.Factory.create()

    //Firestore
    var firestore = FirestoreService()

    //views
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var loginFacebookButton: LoginButton



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
             val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            showMessage("Welcome!")
            finish()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_login)

        //Get Firebase Auth
        auth = Firebase.auth

        //init views
        initViews()

        //init Listeners
        initListeners()



       loginFacebook()
       // loginManagerFacebook()

    }


    private fun initViews()
    {
        emailEditText = findViewById<EditText>(R.id.etLogEmail)
        passwordEditText = findViewById<EditText>(R.id.etLogPassword)
        registerButton = findViewById<Button>(R.id.btnLogSignup)
        loginButton = findViewById<Button>(R.id.btnLogin)
        progressBar = findViewById<ProgressBar>(R.id.pbLogloading)
        loginFacebookButton = findViewById<LoginButton>(R.id.btnFacebookLogin);
        //loginFacebookButton.setReadPermissions("email", "public_profile");
    }


    private fun initListeners() {
        loginButton.setOnClickListener{
            //Track login button click
            Firebase.analytics.logEvent("loginButtonClick", null)
            val email = emailEditText.text.toString()
            if (!isEmailValid(email))
            {
                Log.i("LoginActivity", "Email not valid")
                showMessage(getString(R.string.invalid_username))
                findViewById<EditText>(R.id.etLogEmail).error = getString (R.string.invalid_username)
                return@setOnClickListener
            }
            val password = passwordEditText.text.toString()
            if (!isPasswordValid(password))
            {
                Log.i("LoginActivity", "Password not valid")
                // showMessage(getString(R.string.invalid_password))
                findViewById<EditText>(R.id.etLogPassword).error = getString (R.string.incorrect_password)
                return@setOnClickListener
            }
            login(email, password)
            }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        }



    private fun login(email: String, password: String)
    {
        //Show Loading
        progressBar.visibility = View.VISIBLE
        loginButton.isEnabled = false

       auth.signInWithEmailAndPassword(email, password)
           .addOnCompleteListener {
               //After 2 seconds, this will ve called with the result
               if (it.isSuccessful) {
                   Log.i("LoginActivity", "User Loged!")
                   showMessage("Welcome!")
                   val intent = Intent(this, MainActivity::class.java)
                   startActivity(intent)
                   //Show Loading
                   progressBar.visibility = View.GONE
                   loginButton.isEnabled = true
                   finish()
               }
               else {
                   Log.i("LoginActivity", "Error: ${it.exception}")
                   showMessage("Error signing in ${it.exception?.message ?: ""}")
                   progressBar.visibility = View.GONE
                   loginButton.isEnabled = true
               }
           }

    }


    private fun isEmailValid(email: String) : Boolean {
        val emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"

        return email.isNotBlank()
                && email.contains("@")
                && email.contains(Regex(emailRegex))

    }
    private fun isPasswordValid(password: String) : Boolean {

        return password.isNotBlank()
    }


    private fun containsLetterAndNumber(text: String): Boolean {
        var containsLetter = false
        var containsNumber = false

        text.forEach {
            if(it.isDigit())
            {
                containsNumber = true
            }
            if (it.isLetter())
            {
                containsLetter = true
            }
        }
        return containsLetter && containsNumber
    }


    private fun showMessage(text: String)
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }


    private fun loginFacebook()
    {


        loginFacebookButton.setPermissions("email", "public_profile")

        // Callback registration
        loginFacebookButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                // App code
                Log.d("FacebookLogin", "facebook:onSuccess:$loginResult")
                //changeToMainActivity()
                // handleFacebookAccessToken(loginResult.accessToken)
                loginResult?.let{
                    val token = it.accessToken
                    val credential = FacebookAuthProvider.getCredential(token.token)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                        //Track login facebook button click
                        Firebase.analytics.logEvent("loginButtonFacebookClick", null)

                        if (it.isSuccessful) {
                            Log.i("FacebookLogin", "User Loged!")
                            showMessage("Welcome!")
                            //val intent = Intent(this, MainActivity::class.java)
                            //startActivity(intent)
                            auth.currentUser?.uid?.let { userId ->
                                if (!firestore.checkIfUserExist(userId)) {
                                    Log.d("FacebookLogin", "User created")
                                    var userFire : FirebaseUser? = auth.currentUser
                                    // Create User Model
                                    var user = User()
                                    user.userId = userId
                                    if (userFire != null) {
                                        user.username = userFire.displayName.toString()
                                    }
                                    else user.username = "No name"
                                    user.avatar = "https://media.mmo-champion.com/images/news/2018/february/WoWIcon17.jpg"
                                    showMessage("Welcome!")
                                    firestore.setNewUser(user)
                                }
                            }
                            changeToMainActivity()
                        }
                        else {
                            Log.i("FacebookLogin", "Error: ${it.exception}")
                            showMessage("Error signing in ${it.exception?.message ?: ""}")

                        }
                    }

                }

            }

            override fun onCancel() {
                // App code
                Log.d("FacebookLogin", "facebook:onCancel")
            }

            override fun onError(exception: FacebookException) {
                // App code
                Log.d("FacebookLogin", "facebook:onError", exception)
            }
        })

    }

    private fun loginManagerFacebook()
    {

        loginFacebookButton.setPermissions("email", "public_profile")


        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // App code
                    Log.d("FacebookLogin", "facebook:onSuccess:$loginResult")
                    //changeToMainActivity()
                   // handleFacebookAccessToken(loginResult.accessToken)
                    loginResult?.let{
                        val token = it.accessToken
                        val credential = FacebookAuthProvider.getCredential(token.token)

                        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{
                            if (it.isSuccessful) {
                                Log.i("FacebookLogin", "User Loged!")
                                showMessage("Welcome!")
                                //val intent = Intent(this, MainActivity::class.java)
                                //startActivity(intent)
                                auth.currentUser?.uid?.let { userId ->
                                    if (!firestore.checkIfUserExist(userId)) {
                                        // Create User Model
                                        var user = User()
                                        user.userId = userId
                                        user.username = "asda"
                                        user.avatar =
                                            "https://media.mmo-champion.com/images/news/2018/february/WoWIcon17.jpg"
                                        showMessage("Welcome!")
                                        firestore.setNewUser(user)
                                    }
                                }
                            }
                            else {
                                Log.i("FacebookLogin", "Error: ${it.exception}")
                                showMessage("Error signing in ${it.exception?.message ?: ""}")

                            }
                        }

                    }

                }

                override fun onCancel() {
                    // App code
                    Log.d("FacebookLogin", "facebook:onCancel")
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    Log.d("FacebookLogin", "facebook:onError", exception)
                }
            })

    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)


    }


    private fun changeToMainActivity()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}