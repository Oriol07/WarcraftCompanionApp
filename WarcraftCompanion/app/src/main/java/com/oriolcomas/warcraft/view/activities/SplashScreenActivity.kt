package com.oriolcomas.warcraft.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.oriolcomas.warcraft.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val  animation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        ivLogoWow.startAnimation(animation)

        val intent = Intent(this, LoginActivity::class.java)

        animation.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                startActivity(intent)
                finish() // finish() destrueix activity
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })

    }


}