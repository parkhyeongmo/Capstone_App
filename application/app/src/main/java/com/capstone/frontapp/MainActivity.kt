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
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.common.util.Utility
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

// Node.js 서버 통신 설정 싱글톤 패턴으로 생성
object RetrofitClass {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://a592-115-91-214-3.ngrok.io")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(APIInterface::class.java)
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 카카오 로그인
        val btn_kakao_login = findViewById<ImageButton>(R.id.btn_kakao_login)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("TAG", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // 카카오 로그인 버튼 이벤트
        btn_kakao_login.setOnClickListener{
            // 카카오톡으로 로그인 시도
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("TAG", "카카오톡으로 로그인 실패", error)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 카카오톡으로 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    }
                    else if (token != null) {
                        Log.i("TAG", "카카오계정으로 로그인 성공 ${token.accessToken}")
                        val intent = Intent(this, UserActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            // 카카오톡으로 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
            else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }

    }
}