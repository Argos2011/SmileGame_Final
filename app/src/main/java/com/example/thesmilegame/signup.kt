package com.example.thesmilegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import com.example.thesmilegame.Models.ErrorEnum
import com.example.thesmilegame.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class signup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = Firebase.auth
        database = Firebase.database.reference
        didClickMainMenuButton()
        didSetUpSignUp()
    }

    fun didSetUpSignUp() {
        val mainmenuBtn = findViewById<ImageButton>(R.id.btnSignUpForm)

        mainmenuBtn.setOnClickListener {
            didSignUp()
        }
    }

    fun didSignUp() {
        val fNameTxt = findViewById<EditText>(R.id.fNameTxt)
        val lNameTxt = findViewById<EditText>(R.id.lNameTxt)
        val emailTxt = findViewById<EditText>(R.id.emailSignUpTxt)
        val passwordTxt = findViewById<EditText>(R.id.passwordSignUpTxt)

        val fName = fNameTxt.text.toString()
        val lName = lNameTxt.text.toString()
        val email = emailTxt.text.toString()
        val password = passwordTxt.text.toString()

        val validator = isValidForm(fName, lName, email, password)
        if (validator != ErrorEnum.NON) {
            Toast.makeText(baseContext, validator.error,
                Toast.LENGTH_SHORT).show()
        } else {
            firbaseSignUp(fName, lName,email, password)
        }
    }

    fun firbaseSignUp(fName: String, lName: String, email: String, password: String) {
        val loader = findViewById<ProgressBar>(R.id.signupLoader)

        loader.isVisible = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                loader.isVisible = false
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "createUserWithEmail:success")
                    val userId = auth.currentUser?.getUid()
                    if (userId != null) {
                        writeNewUser(userId,fName, lName, email)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun writeNewUser(userId: String, fname: String, lname: String, email: String) {
        val user = User(fname, lname, email)

        database.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                goToWelcome()
            }
            .addOnFailureListener {
                Log.d("TAG", "FAIL TO WRITE")
            }

    }

    fun goToWelcome() {
        val fNameTxt = findViewById<EditText>(R.id.fNameTxt)
        val lNameTxt = findViewById<EditText>(R.id.lNameTxt)
        val emailTxt = findViewById<EditText>(R.id.emailSignUpTxt)
        val passwordTxt = findViewById<EditText>(R.id.passwordSignUpTxt)
        fNameTxt.setText("")
        lNameTxt.setText("")
        emailTxt.setText("")
        passwordTxt.setText("")
        val intent = Intent(this, user_welcome::class.java)
        startActivity(intent)
    }

    fun isValidForm(fName: String, lName: String, email: String, passowrd: String): ErrorEnum {

        if (fName == "") {
            return ErrorEnum.FIRST_NAME
        }

        if (lName == "") {
            return ErrorEnum.LAST_NAME
        }

        if (email == "") {
            return ErrorEnum.EMAIL
        }

        if (passowrd == "") {
            return  ErrorEnum.PASSWORD
        }

        return ErrorEnum.NON
    }

    fun didClickMainMenuButton() {
        val mainmenuBtn = findViewById<Button>(R.id.btnSignIn)

        mainmenuBtn.setOnClickListener {
            finish()
        }
    }
}