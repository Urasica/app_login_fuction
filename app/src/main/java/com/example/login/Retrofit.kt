package com.example.login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("signup")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>
}

data class SignupResponse(
    val message: String
)