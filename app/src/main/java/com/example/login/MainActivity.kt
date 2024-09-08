package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val loginService by lazy { LoginService(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkTokenAndReLogin()

        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    // 각 기능에 아래 같은 코드 작성해서 로그인해야 진행할 수 있게, 로그인 화면으로 이동 시키기
    // 로그인 하지 않으면 처리 할 수 없도록 구성
    private fun checkTokenAndReLogin() {
        loginService.checkToken { isValid ->
            if (!isValid) {
                // 토큰이 유효하지 않음, 재로그인 처리
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}