package com.example.quizgame.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startQuizBtn.setOnClickListener {
            val intent = Intent(this , QuizGameActivity::class.java)
            startActivity(intent)
        }

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()

            val googleSignInClient = GoogleSignIn.getClient(this,gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this, "Sign out is successfully", Toast.LENGTH_SHORT).show()
                }
            }

            val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}