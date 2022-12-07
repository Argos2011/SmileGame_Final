package com.example.thesmilegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.thesmilegame.Models.ErrorEnum
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        supportActionBar?.hide()
        setupLogInBtn()
        didClickSignupButton()

    }

    override fun onStart() {
        super.onStart()

        var user = Firebase.auth.currentUser
        val currentUser = user//auth.currentUser
        Log.d("TAG", "USER INFO ${currentUser}")
        if(currentUser != null){
            goToWelcome()
        }


    }

    fun setupLogInBtn() {
        val loginBtn = findViewById<ImageButton>(R.id.btnLogin)

        loginBtn.setOnClickListener {
            didClickLoginButton()
        }
    }
    fun didClickLoginButton() {
        val emailTxt = findViewById<EditText>(R.id.emailSignUpTxt)
        val passwordTxt = findViewById<EditText>(R.id.passwordtxt)

        var email = emailTxt.text.toString()
        var password = passwordTxt.text.toString()

        val validator =  isValidForm(email, password)
        if ( validator != ErrorEnum.NON) {
            Toast.makeText(baseContext, validator.error,
                Toast.LENGTH_SHORT).show()
        } else {
            firbaseSignIn(email, password)
        }

        email = ""
        password = ""
        emailTxt.setText("")
        passwordTxt.setText("")
    }

    fun didClickSignupButton() {
        val signupBtn = findViewById<Button>(R.id.btnSignUp)

        signupBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, signup::class.java)
            startActivity(intent)
        }
    }

    fun goToWelcome() {
        val emailTxt = findViewById<EditText>(R.id.emailSignUpTxt)
        val passwordTxt = findViewById<EditText>(R.id.passwordtxt)
        emailTxt.setText("")
        passwordTxt.setText("")
        val intent = Intent(this@MainActivity, user_welcome::class.java)
        startActivity(intent)
    }

    fun firbaseSignIn(email: String, password: String) {
        val signInLoader = findViewById<ProgressBar>(R.id.signInLoader)
        signInLoader.isVisible = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                signInLoader.isVisible = false
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                        goToWelcome()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun isValidForm(email: String, password: String): ErrorEnum {
        if (email == "") {
            return ErrorEnum.EMAIL
        }

        if (password == "") {
            return ErrorEnum.PASSWORD
        }

        return ErrorEnum.NON
    }
}