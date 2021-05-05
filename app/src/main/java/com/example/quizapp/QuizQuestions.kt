package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.quizapp.databinding.ActivityQuizQuestionsBinding

class QuizQuestions : AppCompatActivity() {

    private lateinit var binding: ActivityQuizQuestionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val questionsList = Constants.getQuestions()
        Log.i("Question Size", "${questionsList.size}")
    }
}