package com.example.thesmilegame

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.net.toUri
import androidx.core.view.isVisible
import coil.load
import com.example.thesmilegame.Api.ApiInterface
import com.example.thesmilegame.Api_Helper.Utilities
import com.example.thesmilegame.Models.Smile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class gameplay : AppCompatActivity() {

    private var solution = ""
    private var question = ""
    private var health   = 3
    private var score    = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameplay)

        supportActionBar?.hide()

        didClickSettings()
        getSmileData()
        didClickSubmitBtn()
        didClickAddButton()
        didClickSubBtn()
    }

    fun didClickSubmitBtn() {
        val loginBtn = findViewById<ImageButton>(R.id.btnSubmit)
        val answerText     = findViewById<EditText>(R.id.answerTxt)
        loginBtn.setOnClickListener {
            if ( answerText.text.toString() != "") {
                if (answerText.text.toString() == solution.toString()) {
                    Toast.makeText(applicationContext, "Congratulations !!!!...",     Toast.LENGTH_SHORT).show()
                    answerText.setText("0")
                    setScore()
                    getSmileData()
                } else {
                    Toast.makeText(applicationContext, "Incorrect answer !!!!...",     Toast.LENGTH_SHORT).show()
                    reduceLifes()
                }
            }
        }
    }

    fun getSmileData() {
        val retrofitBuider = Utilities.getInstance()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuider.getDataSmile()
        val loaderView = findViewById<ProgressBar>(R.id.loader)

        loaderView.isVisible = true
        retrofitData.enqueue(object : Callback<Smile?> {
            override fun onResponse(call: Call<Smile?>, response: Response<Smile?>) {
                    val result = response.body()
                solution = result?.solution.toString()
                question = result?.question.toString()
                DownloadImageFromInternet(findViewById(R.id.gameImgView), loaderView).execute(question)
                Log.d("gamePlay", "Data ${solution}")
            }

            override fun onFailure(call: Call<Smile?>, t: Throwable) {
                    Log.d("gamePlay", "Fail "+t.message)
            }
        })
    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView, var loaderView: ProgressBar) : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            loaderView.isVisible = false
            imageView.setImageBitmap(result)
        }
    }

    fun setScore() {
        val scoreTxt     = findViewById<TextView>(R.id.scoreTxt)
        score += 10
        scoreTxt.text = score.toString()
    }

    fun reduceLifes() {
        val h1     = findViewById<ImageView>(R.id.h1)
        val h2     = findViewById<ImageView>(R.id.h2)
        val h3     = findViewById<ImageView>(R.id.h3)

        health -= 1
        when (health) {
            2 -> {
                h3.isVisible = false
            }
            1 -> {
                h3.isVisible = false
                h2.isVisible = false
            }
            0 -> {
                h3.isVisible = false
                h2.isVisible = false
                h1.isVisible = false
                showAlert("Game Over", "Do you want to retry?")
            }
            else -> {
                h3.isVisible = true
                h2.isVisible = true
                h1.isVisible = true
            }
        }
    }

    fun resetGame() {
        val h1     = findViewById<ImageView>(R.id.h1)
        val h2     = findViewById<ImageView>(R.id.h2)
        val h3     = findViewById<ImageView>(R.id.h3)
        val answerField     = findViewById<EditText>(R.id.answerTxt)
        val scoreTxt        = findViewById<TextView>(R.id.scoreTxt)

        h3.isVisible = true
        h2.isVisible = true
        h1.isVisible = true

        health = 3
        score  = 0

        answerField.setText("0")
        scoreTxt.setText(score)
        getSmileData()
    }

    fun setAnswer(shouldIncrease: Boolean) {
        val answerField     = findViewById<EditText>(R.id.answerTxt)
        var answer =  answerField.text.toString().toInt()
        if ( shouldIncrease && (answer.toInt() != 9) ) {
            answer += 1
        } else if (answer.toInt() != 0 ) {
            answer -= 1
        }
        answerField.setText(answer.toString())
    }

    fun didClickAddButton() {
        val addBtn = findViewById<ImageButton>(R.id.addBtn)
        addBtn.setOnClickListener {
            setAnswer(true)
        }
    }

    fun didClickSubBtn() {
        val subBtn = findViewById<ImageButton>(R.id.subBtn)
        subBtn.setOnClickListener {
            setAnswer(false)
        }
    }

    fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle(title)
        //set message for alert dialog
        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Retry"){dialogInterface, which ->
            resetGame()
        }
        //performing negative action
        builder.setNegativeButton("Exit"){dialogInterface, which ->
            finish()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun didClickSettings() {
        val settingsBtn = findViewById<ImageButton>(R.id.settingsBtn)
        settingsBtn.setOnClickListener {
            finish()
        }
    }
}