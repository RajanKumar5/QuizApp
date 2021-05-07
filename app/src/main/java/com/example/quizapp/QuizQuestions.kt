package com.example.quizapp

import android.app.Instrumentation
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.quizapp.databinding.ActivityQuizQuestionsBinding
import org.w3c.dom.Text

class QuizQuestions : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityQuizQuestionsBinding

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var hasAnsweredTheQuestion: Boolean = false
    private var isAnswerEmpty: Boolean = true
    private var totalCorrectAnswers: Int = 0
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        username = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

    }

    private fun setQuestion() {
        val question = mQuestionsList!![mCurrentPosition - 1]

        defaultOptionsView()
        hasAnsweredTheQuestion = false
        isAnswerEmpty = true

        if (mCurrentPosition == mQuestionsList!!.size) {
            binding.btnSubmit.text = "FINISH"
        } else {
            binding.btnSubmit.text = "SUBMIT"
        }

        binding.progressBar.progress = mCurrentPosition
        binding.ivImage.setImageResource(question!!.image)
        binding.tvProgressBar.text = "${mCurrentPosition}/${binding.progressBar.max}"

        binding.tvQuestion.text = question!!.question
        binding.tvOptionOne.text = question!!.optionOne
        binding.tvOptionTwo.text = question!!.optionTwo
        binding.tvOptionThree.text = question!!.optionThree
        binding.tvOptionFour.text = question!!.optionFour
    }

    private fun defaultOptionsView() {
        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNumber: Int) {
        if (!hasAnsweredTheQuestion) {
            defaultOptionsView()
            mSelectedOptionPosition = selectedOptionNumber

            tv.setTextColor(Color.parseColor("#363A43"))
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.background = ContextCompat.getDrawable(
                this,
                R.drawable.selected_option_border_bg
            )
            isAnswerEmpty = false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvOptionOne -> {
                selectedOptionView(binding.tvOptionOne, 1)
            }
            R.id.tvOptionTwo -> {
                selectedOptionView(binding.tvOptionTwo, 2)
            }
            R.id.tvOptionThree -> {
                selectedOptionView(binding.tvOptionThree, 3)
            }
            R.id.tvOptionFour -> {
                selectedOptionView(binding.tvOptionFour, 4)
            }
            R.id.btnSubmit -> {
                if (isAnswerEmpty) {
                    Toast.makeText(
                        this,
                        "You must answer the question",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (mSelectedOptionPosition == 0) {
                        mCurrentPosition++

                        when {
                            mCurrentPosition <= mQuestionsList!!.size -> {
                                setQuestion()
                            }
                            else -> {
                                val intent = Intent(this, ResultActivity::class.java)
                                intent.putExtra(Constants.USER_NAME, username)
                                intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                                intent.putExtra(Constants.CORRECT_ANSWERS, totalCorrectAnswers)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        val questionAnswer = mQuestionsList!![mCurrentPosition - 1].correctAnswer
                        if (questionAnswer != mSelectedOptionPosition) {
                            answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
                        } else {
                            totalCorrectAnswers++
                        }
                        answerView(questionAnswer, R.drawable.correct_option_border_bg)
                        hasAnsweredTheQuestion = true

                        if (mCurrentPosition == mQuestionsList!!.size) {
                            binding.btnSubmit.text = "FINISH"
                        } else {
                            binding.btnSubmit.text = "GO TO NEXT QUESTION"
                        }
                        println(mSelectedOptionPosition)
                        mSelectedOptionPosition = 0
                    }
                }
            }

        }
    }

    private fun answerView(answer: Int, drawableView: Int) {
        when (answer) {
            1 -> {
                binding.tvOptionOne.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            2 -> {
                binding.tvOptionTwo.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            3 -> {
                binding.tvOptionThree.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
            4 -> {
                binding.tvOptionFour.background = ContextCompat.getDrawable(
                    this,
                    drawableView
                )
            }
        }
    }

}