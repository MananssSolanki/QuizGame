package com.example.quizgame.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizgame.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignUpBinding

    var auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            val email = binding.emailEdt.text.toString()
            val password = binding.passwordEdt.text.toString()

            signupWithFirebase(email , password)
        }

    }

    fun signupWithFirebase(email : String, password : String){
        binding.progressBar.visibility = View.VISIBLE
        binding.signUpBtn.isClickable = false

        auth.createUserWithEmailAndPassword(email , password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this, "Your account has been created", Toast.LENGTH_SHORT).show()
                finish()
                binding.progressBar.visibility = View.GONE
                binding.progressBar.isClickable = true
            }else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}