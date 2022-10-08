package com.capstone.frontapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.common.util.Utility

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn_kakao_login = findViewById<ImageButton>(R.id.btn_kakao_login)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("TAG", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
                val intent = Intent(this, AccountActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        btn_kakao_login.setOnClickListener{

            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)

        }


    }
}