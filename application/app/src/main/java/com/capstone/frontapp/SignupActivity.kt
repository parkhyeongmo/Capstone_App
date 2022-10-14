package com.capstone.frontapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.EditText

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // 전화번호 입력 폼 양식 설정
        val phone_num = findViewById<EditText>(R.id.edit_phone_num)
        phone_num.addTextChangedListener(PhoneNumberFormattingTextWatcher())




    }
}