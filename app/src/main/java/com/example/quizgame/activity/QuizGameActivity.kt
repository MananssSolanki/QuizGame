package com.example.quizgame.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityQuizGameBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuizGameActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("questions")

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 1

    lateinit var binding: ActivityQuizGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gameLogic()

        binding.nextBtn.setOnClickListener {
            gameLogic()
        }

        binding.finishBtn.setOnClickListener {

        }

        binding.option1Txt.setOnClickListener {

        }

        binding.option2Txt.setOnClickListener {

        }

        binding.option3Txt.setOnClickListener {

        }

        binding.option4Txt.setOnClickListener {

        }


    }

    fun gameLogic() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber <= questionCount) {

                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    answerA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    answerB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    answerC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    answerD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    correctAnswer =
                        snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    binding.questionTxt.text = question
                    binding.option1Txt.text = answerA
                    binding.option2Txt.text = answerB
                    binding.option3Txt.text = answerC
                    binding.option4Txt.text = answerD

                    binding.progressBar.visibility = View.INVISIBLE
                    binding.headerTitle.visibility = View.VISIBLE
                    binding.mainLy.visibility = View.VISIBLE
                    binding.butonLy.visibility = View.VISIBLE
                }else{
                    Toast.makeText(this@QuizGameActivity, "you answer all the question", Toast.LENGTH_SHORT).show()

                }

                questionNumber++


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizGameActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }
}