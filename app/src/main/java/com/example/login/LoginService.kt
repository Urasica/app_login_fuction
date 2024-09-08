package com.example.login

import android.content.Context
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class   LoginService(private val context: Context) {

    private val tokenManager = TokenManager(context)
    private val client = OkHttpClient()

    // 로그인 기능, 아이디와 비번 서버로 전달해서 토큰 받고 저장
    fun login(username: String, password: String, callback: (Boolean, String?) -> Unit) {
        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
        }
        val requestBody = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://35.216.0.159:8080/auth/login")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val json = JSONObject(responseBody)
                    val token = json.getString("token")

                    // JWT 토큰을 SharedPreferences에 저장
                    tokenManager.saveToken(token)
                    callback(true, token)
                } else {
                    callback(false, null)
                }
            }
        })
    }

    // 토큰이 정상적인지 확인하는 기능. 이 함수는 Boolean 값만 반환, 처리는 메인 액티비티의 코드 참조
    fun checkToken(callback: (Boolean) -> Unit) {
        val token = tokenManager.getToken()

        if (token != null) {
            val request = Request.Builder()
                .url("http://35.216.0.159:8080/auth/check-token")
                .header("Authorization", "Bearer $token")
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback(response.isSuccessful)
                }
            })
        } else {
            callback(false)
        }
    }
}
