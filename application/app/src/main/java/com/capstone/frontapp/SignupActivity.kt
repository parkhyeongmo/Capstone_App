package com.capstone.frontapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.EditText

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val phone_num = findViewById<EditText>(R.id.phone_num)
        phone_num.addTextChangedListener(PhoneNumberFormattingTextWatcher())




    }
}