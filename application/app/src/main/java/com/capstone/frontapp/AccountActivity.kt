package com.capstone.frontapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val intent = Intent(this, MainActivity::class.java)

        // 상단바 사용자 이름 설정
        val nameSet = findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 카카오 로그아웃 버튼
        val btn_logout = findViewById<Button>(R.id.btn_logout)
        btn_logout.setOnClickListener{

            // 카카오 로그아웃
            UserApiClient.instance.logout { error ->
                if(error != null){
                    Log.e("로그아웃", "실패, token 삭제됨", error)
                }

                else{
                    Log.i("로그아웃", "성공, token 삭제됨")
                    UserInfo.id = null
                    UserInfo.type = null
                    UserInfo.name = null
                    UserInfo.jwt = null

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // 로그아웃 시 Stack의 이전 활동 전부 종료
                    startActivity(intent)
                    finish()
                }
            }

        }

        val successToast = Toast.makeText(this, "회원탈퇴 성공", Toast.LENGTH_SHORT)
        val failToast = Toast.makeText(this, "회원탈퇴 fail", Toast.LENGTH_SHORT)

        // 카카오 회원탈퇴 버튼
        var btn_unlink = findViewById<Button>(R.id.btn_unlink)
        btn_unlink.setOnClickListener{

            // 계정 회원 탈퇴
            RetrofitClass.api.unLink(UserInfo.jwt.toString())!!.enqueue(object : Callback<result> {

                override fun onResponse(call: Call<result>, response: Response<result>) {
                    Log.i("회원탈퇴", "성공" + response.body()?.rst)
                    UserInfo.id = null
                    UserInfo.type = null
                    UserInfo.name = null
                    UserInfo.jwt = null

                    Log.i("회원탈퇴", "id:${UserInfo.id}, name:${UserInfo.name}, jwt:${UserInfo.jwt}")

                    UserApiClient.instance.unlink { error ->

                        if(error != null) {
                            Log.e("탈퇴", "실패")
                        }
                        else{
                            Log.i("탈퇴", "성공")
                            successToast.show()
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // 로그아웃 시 Stack의 이전 활동 전부 종료
                            startActivity(intent)
                            finish()
                        }

                    }
                }

                override fun onFailure(call: Call<result>, t: Throwable) {
                    Log.i("회원탈퇴", "실패" + t.message.toString())
                    failToast.show()
                }

            })

        }

        // 뒤로가기 버튼
        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            onBackPressed()
        }

    }
}