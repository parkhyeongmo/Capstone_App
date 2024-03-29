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
import com.kakao.sdk.user.model.User
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

// Node.js 서버 통신 설정 싱글톤 패턴으로 생성
object RetrofitClass {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://3.35.30.57")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(APIInterface::class.java)
}

object UserInfo {
    var id : String? = null
    var name : String? = null
    var type : Int? = null
    var jwt : String? = null
}

class MainActivity : AppCompatActivity() {

    private fun logIn(accessToken: String) {
        // Node.js 서버에 로그인 요청
        RetrofitClass.api.logIn(accessToken)!!.enqueue(object : retrofit2.Callback<loginResponse>{
            override fun onResponse(
                call: Call<loginResponse>,
                response: Response<loginResponse>
            ) {
                if (response.isSuccessful && response.code() == 200){ // 로그인 성공

                    Log.i("로그인", "성공")
                    UserInfo.id = response.body()?.id
                    UserInfo.name = response.body()?.name
                    UserInfo.type = response.body()?.type
                    UserInfo.jwt = response.body()?.jwt
                    Log.i("로그인", "${UserInfo.id} + ${UserInfo.name} + ${UserInfo.type} + ${UserInfo.jwt}")

                    if (UserInfo.type == 0) {
                        val userIntent = Intent(this@MainActivity, UserActivity::class.java)
                        startActivity(userIntent)
                        finish()
                    }

                    else if (UserInfo.type == 1) {
                        val ExpertIntent = Intent(this@MainActivity, ExpertActivity::class.java)
                        startActivity(ExpertIntent)
                        finish()
                    }
                }
                else { // 비회원인 경우 회원가입 액티비티로 진행
                    val SignUpIntent = Intent(this@MainActivity, SignupActivity::class.java)
                    Log.i("로그인", "비회원 계정 ${response.body()?.name}")
                    SignUpIntent.putExtra("accessToken", accessToken)
                    startActivity(SignUpIntent)
                    finish()
                }
            }
            override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                Log.i("로그인", "실패")
            }
        })
    }

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

                // 로그인 함수 호출
                logIn(token.accessToken)
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
                        Log.i("TAG", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        logIn(token.accessToken)
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