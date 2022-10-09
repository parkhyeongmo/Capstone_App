package com.capstone.frontapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.kakao.sdk.user.UserApiClient
import kotlin.math.log

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        UserApiClient.instance.me { user, error ->

            if(error != null)
                Log.e("실패", "사용자 요청 실패", error)

            else if(user != null){

                Log.i("정보", "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

            }

        }

        // 카카오 로그아웃 버튼
        var btn_logout = findViewById<Button>(R.id.btn_logout)
        btn_logout.setOnClickListener{

            UserApiClient.instance.logout { error ->

                if(error != null){
                    Log.e("로그아웃", "실패, token 삭제됨", error)
                }

                else{
                    Log.i("로그아웃", "성공, 토큰 삭제됨")

                    val intent = Intent(this, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // 로그아웃 시 Stack의 이전 활동 전부 종료
                    startActivity(intent)
                    finish()

                }

            }

        }

        // 카카오 회원탈퇴 버튼
        var btn_unlink = findViewById<Button>(R.id.btn_unlink)
        btn_unlink.setOnClickListener{

            UserApiClient.instance.unlink { error ->

                if(error != null) {
                    Log.e("탈퇴", "실패", error)
                    Toast.makeText(this, "탈퇴 실패", Toast.LENGTH_SHORT).show()

                }
                else{

                    Log.i("탈퇴", "성공")
                    Toast.makeText(this, "탈퇴 성공", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }

            }

        }


    }
}