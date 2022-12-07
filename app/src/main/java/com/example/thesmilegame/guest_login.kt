package com.example.thesmilegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class guest_login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_login)

        
        supportActionBar?.hide()
    }
}