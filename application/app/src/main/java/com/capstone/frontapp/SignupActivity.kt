package com.capstone.frontapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.kakao.sdk.user.UserApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // MainActivity에서 accessToken 받아오기
        var getData = intent.getStringExtra("accessToken")
        Log.i("겟 토큰", getData.toString())

        // 전화번호 입력 폼 양식 설정
        val phone_num = findViewById<EditText>(R.id.edit_phone_num)
        phone_num.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        val btn_signup = findViewById<Button>(R.id.btn_signup)
        val btn_cancel = findViewById<Button>(R.id.btn_cancel)

        var accountType : Int = 0
        findViewById<RadioGroup>(R.id.type_radio_group).setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radio_btn_user -> accountType = 0
                R.id.radio_btn_expert -> accountType = 1
            }
        }


        val empNumEdit = findViewById<EditText>(R.id.edit_emp_num)
        val phoneEdit = findViewById<EditText>(R.id.edit_phone_num)

        // 회원가입 버튼
        btn_signup.setOnClickListener {
            val tmp = empNumEdit.text.toString()
            val empNum = tmp.toInt()
            val phoneNum = phoneEdit.text.toString()

            var userInfo = HashMap<String, Any>()
            userInfo.put("type", accountType)
            userInfo.put("empnum", empNum)
            userInfo.put("hp", phoneNum)

            Log.i("버튼 터치", "${userInfo.get("type")} + ${userInfo.get("empnum")} + ${userInfo.get("hp")}")

            RetrofitClass.api.signUp(getData.toString(), userInfo)!!.enqueue(object : Callback<signUp> {

                override fun onResponse(call: Call<signUp>, response: Response<signUp>) {
                    if(response.isSuccessful) {
                        Log.i("회원가입", "성공")
                    }
                    else {
                        Log.i("회원가입", "실패")
                    }

                }

                override fun onFailure(call: Call<signUp>, t: Throwable) {
                    Log.i("회원가입", "응답 실패")
                }
            })

        }

        //회원가입 취소 버튼
        btn_cancel.setOnClickListener {
            Log.i("회원가입", "취소")
            Toast.makeText(this, "회원가입 취소", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

    }
}