package com.oriolcomas.warcraft.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.oriolcomas.warcraft.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val MIN_PASSWORD_LENGTH = 6

    //views
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

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
        emailEditText = findViewById<EditText>(R.id.etUsername)
        passwordEditText = findViewById<EditText>(R.id.etPassword)
        registerButton = findViewById<Button>(R.id.btnSignup)
    }


    private fun initListeners() {
        registerButton.setOnClickListener {
            //Get email and password from EditTexts.
            val email = emailEditText.text.toString()
            if (!isEmailValid(email))
            {
                Log.i("RegisterActivity", "Email not valid")
               // showMessage(getString(R.string.invalid_username))
                findViewById<EditText>(R.id.etUsername).error = getString (R.string.invalid_username)
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


            //Register user :)
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    //After 2 seconds, this will ve called with the result
                    if (it.isSuccessful) {
                        Log.i("RegisterActivity", "User Registered!")
                    } else {
                        Log.i("RegisterActivity", "Error: ${it.exception}")
                        showMessage("Error signing up ${it.exception?.message ?: ""}")
                    }

                }

            //Do other things
            //..


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

        val passwordRegex = "[a-zA-Z0-9-.]";
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
