package com.example.quizgame.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityQuizGameBinding

class QuizGameActivity : AppCompatActivity() {

    lateinit var binding : ActivityQuizGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizGameBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}