package com.example.quizgame.activity


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding : ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root

        val alphaAnimation = android.view.animation.AnimationUtils.loadAnimation(applicationContext,
            R.anim.splash_anim)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed(object : Runnable{
            override fun run() {
                val intent = Intent(this@WelcomeActivity , LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        },5000)
        setContentView(view)
    }
}