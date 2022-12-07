package com.example.thesmilegame

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.thesmilegame.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class user_welcome : AppCompatActivity() {

    internal var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_welcome)

        didclickNewGameButton()
        didClickLogOutButton()
    }

    override fun onStart() {
        super.onStart()
        getLogedUser()
    }

        fun didclickNewGameButton() {
            val newgameBtn = findViewById<ImageButton>(R.id.btnNewGame_UserWelcome)

            newgameBtn.setOnClickListener {
                val intent = Intent(this@user_welcome, gameplay::class.java)
                startActivity(intent)
            }
        }

    fun didClickLogOutButton() {
        val btnLogOut = findViewById<Button>(R.id.btnLogOut)

        btnLogOut.setOnClickListener {
            showAlert()
        }
    }

    fun userLogOut() {
        Firebase.auth.signOut()
    }

    fun showAlert() {
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Log Out")
        //set message for alert dialog
        builder.setMessage("Are you sure you want to log out?")
        builder.setIcon(android.R.drawable.ic_delete)

        //performing positive action
        builder.setPositiveButton("No"){dialogInterface, which ->

        }
        //performing negative action
        builder.setNegativeButton("Yes"){dialogInterface, which ->
            userLogOut()
            finish()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun getLogedUser() {  // declare user object outside onCreate Method
        val welcomeLoader = findViewById<ProgressBar>(R.id.userWelcomeLoader)
        val authUser = Firebase.auth.currentUser
        val userId = authUser?.getUid()!!
        val emailId = authUser?.email
        val rootRef = FirebaseDatabase.getInstance().reference

        welcomeLoader.isVisible = true
        rootRef.child("users").child(userId).get().addOnSuccessListener {
            welcomeLoader.isVisible = false
            Log.i("firebase", "Got value ${it.value}")
            val user = it.getValue(User::class.java)
            Log.i("firebase", "Got value ${user?.lname}")
            val userNameTxt = findViewById<TextView>(R.id.userNameTxt)
            val userName = "${user?.fname} ${user?.lname}"
            userNameTxt.setText(userName)
        }.addOnFailureListener{
            welcomeLoader.isVisible = false
            Log.e("firebase", "Error getting data", it)
        }
    }
}