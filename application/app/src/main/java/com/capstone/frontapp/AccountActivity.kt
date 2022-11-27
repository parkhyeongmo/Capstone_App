package com.capstone.frontapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class AccountActivity : AppCompatActivity() {

    private fun unlink() {
        // 계정 회원 탈퇴
        RetrofitClass.api.unLink(UserInfo.jwt.toString())!!.enqueue(object : retrofit2.Callback<result> {
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
                        Toast.makeText(this@AccountActivity, "회원탈퇴 성공", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@AccountActivity, MainActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // 로그아웃 시 Stack의 이전 활동 전부 종료
                        startActivity(intent)
                        finish()
                    }

                }
            }

            override fun onFailure(call: Call<result>, t: Throwable) {
                Log.i("회원탈퇴", "실패" + t.message.toString())
                Toast.makeText(this@AccountActivity, "회원탈퇴 fail", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logOut() {
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

                val intent = Intent(this@AccountActivity, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // 로그아웃 시 Stack의 이전 활동 전부 종료
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)


        // 상단바 사용자 이름 설정
        val nameSet = findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 카카오 로그아웃 버튼
        val btn_logout = findViewById<Button>(R.id.btn_logout)
        btn_logout.setOnClickListener{
           logOut()
        }

        // 카카오 회원탈퇴 버튼
        var btn_unlink = findViewById<Button>(R.id.btn_unlink)
        btn_unlink.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.unlink_dialog, null)

            builder.setView(dialogView)
                .setPositiveButton("탈퇴하기") { dialogInterface, i ->
                    unlink()
                }
                .setNegativeButton("취소") {dialogInterface, i ->

                }.show()
        }

        // 뒤로가기 버튼
        val btn_back = findViewById<ImageButton>(R.id.btn_back)
        btn_back.setOnClickListener {
            onBackPressed()
        }

    }
}