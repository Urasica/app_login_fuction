package com.example.login

data class SignupRequest(
    val username: String,
    val password: String,
    val name: String,
    val phoneNumber: String
)