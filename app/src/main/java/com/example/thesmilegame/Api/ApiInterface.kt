package com.example.thesmilegame.Api

import com.example.thesmilegame.Models.Smile
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET


interface ApiInterface {

    @GET("uob/smile/api.php")
    suspend fun getData(): Response<Smile>

    @GET("uob/smile/api.php")
    fun getDataSmile(): Call<Smile>
}