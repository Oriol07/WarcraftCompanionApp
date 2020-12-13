package com.oriolcomas.warcraft.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oriolcomas.warcraft.R
import com.oriolcomas.warcraft.model.User
import com.oriolcomas.warcraft.network.FirestoreService

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    //private lateinit

    private val MIN_PASSWORD_LENGTH = 6

    //views
    private lateinit var emailEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var progressBar: ProgressBar

    //Firestore
    var firestore = FirestoreService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Get Firebase Auth
        auth = Firebase.auth


        //init views
        initViews()

        //init listeners
        initListeners()

        }

    private fun initViews()
    {
        usernameEditText = findViewById<EditText>(R.id.etUsername)
        emailEditText = findViewById<EditText>(R.id.etEmail)
        passwordEditText = findViewById<EditText>(R.id.etPassword)
        registerButton = findViewById<Button>(R.id.btnRegSignup)
        progressBar = findViewById<ProgressBar>(R.id.pbRegLoading)
    }


    private fun initListeners() {
        registerButton.setOnClickListener {
            //Get email and password from EditTexts.
            val email = emailEditText.text.toString()
            if (!isEmailValid(email))
            {
                Log.i("RegisterActivity", "Email not valid")
               // showMessage(getString(R.string.invalid_username))
                findViewById<EditText>(R.id.etEmail).error = getString (R.string.invalid_username)
                return@setOnClickListener
            }


            val password = passwordEditText.text.toString()
            if (!isPasswordValid(password))
            {
                Log.i("RegisterActivity", "Password not valid")
               // showMessage(getString(R.string.invalid_password))
                findViewById<EditText>(R.id.etPassword).error = getString (R.string.invalid_password, MIN_PASSWORD_LENGTH)
                return@setOnClickListener
            }

           val username = usernameEditText.text.toString()


            //Register user :)
            registerUser(email, username, password)


            //Do other things
            //..


        }
    }

    private fun registerUser(email:String, username: String, password: String)
    {
        //Show Loading
        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                //After 2 seconds, this will ve called with the result
                if (it.isSuccessful) {
                    Log.i("RegisterActivity", "User Registered!")
                    //showMessage("${R.string.welcome.toString()}")
                    auth.currentUser?.uid?.let{userId ->

                        // Create User Model
                       var user = User()
                        user.userId = userId
                        user.username =  username
                        user.avatar = "https://media.mmo-champion.com/images/news/2018/february/WoWIcon17.jpg"
                        showMessage(R.string.welcome.toString())
                        firestore.setNewUser(user)
                        progressBar.visibility = View.GONE
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } ?: kotlin.run {
                        Log.e("RegisterActivity", "Error: useriD is null!")
                        showMessage("Error signing up ${it.exception?.message ?: ""}")
                        progressBar.visibility = View.GONE
                    }


                } else {
                    Log.i("RegisterActivity", "Error: ${it.exception}")
                    showMessage("Error signing up ${it.exception?.message ?: ""}")
                    progressBar.visibility = View.GONE
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
                && password.count() >= MIN_PASSWORD_LENGTH
                && containsLetterAndNumber(password)
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

}
