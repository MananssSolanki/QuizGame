package com.example.quizgame.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.R
import com.example.quizgame.databinding.ActivityQuizGameBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class QuizGameActivity : AppCompatActivity() {

    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference.child("questions")

    lateinit var timer : CountDownTimer

    private val totalTime = 25000L

    var timerContinue = false

    var leftTime = totalTime

    var question = ""
    var answerA = ""
    var answerB = ""
    var answerC = ""
    var answerD = ""
    var correctAnswer = ""
    var questionCount = 0
    var questionNumber = 0

    var userAnswer = ""
    var userCorrect = 0

    var userWrong =0

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val scoreRef = database.reference

    val questions = HashSet<Int>()

    lateinit var binding: ActivityQuizGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        do {
            val number = Random.nextInt(1,10)
            Log.d("number",number.toString())
            questions.add(number)
        }while (questions.size < 5)

        gameLogic()

        binding.nextBtn.setOnClickListener {
            resetTimer()
            gameLogic()
        }

        binding.finishBtn.setOnClickListener {
            sendScore()
        }

        binding.option1Txt.setOnClickListener {
            pauseTimer()
            userAnswer = "a"
            if(correctAnswer == userAnswer){
                binding.option1Txt.setBackgroundColor(Color.GREEN)
                userCorrect++
                binding.correctTxt.text = userCorrect.toString()
            }else{
                binding.option1Txt.setBackgroundColor(Color.RED)
                userWrong++
                binding.wrongTxt.text = userWrong.toString()
                finAnswer()
            }
            disableClickableOfOptions()
        }

        binding.option2Txt.setOnClickListener {
            pauseTimer()
            userAnswer = "b"
            if(correctAnswer == userAnswer){
                binding.option2Txt.setBackgroundColor(Color.GREEN)
                userCorrect++
                binding.correctTxt.text = userCorrect.toString()
            }else{
                binding.option2Txt.setBackgroundColor(Color.RED)
                userWrong++
                binding.wrongTxt.text = userWrong.toString()
                finAnswer()
            }
            disableClickableOfOptions()
        }

        binding.option3Txt.setOnClickListener {
            pauseTimer()
            userAnswer = "c"
            if(correctAnswer == userAnswer){
                binding.option3Txt.setBackgroundColor(Color.GREEN)
                userCorrect++
                binding.correctTxt.text = userCorrect.toString()
            }else{
                binding.option3Txt.setBackgroundColor(Color.RED)
                userWrong++
                binding.wrongTxt.text = userWrong.toString()
                finAnswer()
            }
            disableClickableOfOptions()
        }

        binding.option4Txt.setOnClickListener {
            pauseTimer()
            userAnswer = "d"
            if(correctAnswer == userAnswer){
                binding.option4Txt.setBackgroundColor(Color.GREEN)
                userCorrect++
                binding.correctTxt.text = userCorrect.toString()
            }else{
                binding.option4Txt.setBackgroundColor(Color.RED)
                userWrong++
                binding.wrongTxt.text = userWrong.toString()
                finAnswer()
            }
            disableClickableOfOptions()
        }


    }

    fun gameLogic() {
        restoreOptions()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber < questions.size){

                    question = snapshot.child(questions.elementAt(questionNumber).toString()).child("q").value.toString()
                    answerA = snapshot.child(questions.elementAt(questionNumber).toString()).child("a").value.toString()
                    answerB = snapshot.child(questions.elementAt(questionNumber).toString()).child("b").value.toString()
                    answerC = snapshot.child(questions.elementAt(questionNumber).toString()).child("c").value.toString()
                    answerD = snapshot.child(questions.elementAt(questionNumber).toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questions.elementAt(questionNumber).toString()).child("answer").value.toString()

                    binding.questionTxt.text = question
                    binding.option1Txt.text = answerA
                    binding.option2Txt.text = answerB
                    binding.option3Txt.text = answerC
                    binding.option4Txt.text = answerD

                    binding.progressBar.visibility = View.INVISIBLE
                    binding.headerTitle.visibility = View.VISIBLE
                    binding.mainLy.visibility = View.VISIBLE
                    binding.butonLy.visibility = View.VISIBLE

                    startTimer()
                }else{
                    val dialogMessage = AlertDialog.Builder(this@QuizGameActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulations!!!\nYou have answered all the questions. Do you want to see the result?")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow,position ->

                        sendScore()

                    }
                    dialogMessage.setNegativeButton("Play Again"){dialogWindow,position ->

                        val intent = Intent(this@QuizGameActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                    dialogMessage.create().show()
                }

                questionNumber++


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizGameActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun finAnswer(){
        when(correctAnswer){
            "a" -> binding.option1Txt.setBackgroundColor(Color.GREEN)
            "b" -> binding.option2Txt.setBackgroundColor(Color.GREEN)
            "c" -> binding.option3Txt.setBackgroundColor(Color.GREEN)
            "d" -> binding.option4Txt.setBackgroundColor(Color.GREEN)
        }
    }

    fun disableClickableOfOptions(){
        binding.option1Txt.isClickable = false
        binding.option2Txt.isClickable = false
        binding.option3Txt.isClickable = false
        binding.option4Txt.isClickable = false
    }

    fun restoreOptions(){
        binding.option1Txt.setBackgroundColor(Color.WHITE)
        binding.option2Txt.setBackgroundColor(Color.WHITE)
        binding.option3Txt.setBackgroundColor(Color.WHITE)
        binding.option4Txt.setBackgroundColor(Color.WHITE)

        binding.option1Txt.isClickable = true
        binding.option2Txt.isClickable = true
        binding.option3Txt.isClickable = true
        binding.option4Txt.isClickable = true

    }

    private fun startTimer(){
        timer = object  : CountDownTimer(leftTime , 1000){
            override fun onFinish() {
                disableClickableOfOptions()
                resetTimer()
                updateCountDownText()
                binding.questionTxt.text = "Sorry Time is up! Continue with next question"
                timerContinue = false
            }

            override fun onTick(millisUntilFinished: Long) {
                leftTime = millisUntilFinished
                updateCountDownText()
            }

        }.start()
        timerContinue = true
    }

    fun updateCountDownText(){
        val remainingTime : Int = (leftTime/1000).toInt()
        binding.timeTxt.text = remainingTime.toString()
    }

    fun pauseTimer(){
        timer.cancel()
        timerContinue = false
    }

    fun resetTimer(){
        pauseTimer()
        leftTime = totalTime
        updateCountDownText()
    }

    fun sendScore(){
        user?.let {
            val userUID = it.uid
            scoreRef.child("scores").child("correct").setValue(userCorrect)
            scoreRef.child("scores").child("wrong").setValue(userWrong)

            Toast.makeText(this, "Scores sent to database successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this , ResultActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}