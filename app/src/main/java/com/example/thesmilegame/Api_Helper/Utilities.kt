package com.example.thesmilegame.Api_Helper

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Utilities {

    var BASE_URL = "https://marcconrad.com/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
}