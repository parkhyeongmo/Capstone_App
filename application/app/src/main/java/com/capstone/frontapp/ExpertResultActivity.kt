package com.capstone.frontapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class ExpertResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expert_result)

        // 출력할 검사 결과 받아오기
        val getData = intent.extras?.get("inspection") as inspectResult

        // 뒤로가기 버튼
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener{
            onBackPressed()
        }

        // 사용자명 표기
        findViewById<TextView>(R.id.user_name).text = UserInfo.name + "님"

        // 검사 결과 이미지 Bitmap으로 Decoding
        val encodeByte = Base64.decode(getData.imgStr, Base64.DEFAULT)
        val img = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)

        // 검사 결과 화면에 반영
        findViewById<ImageView>(R.id.image_result).setImageBitmap(img)
        if (getData.result.isdefected == 0) {
            findViewById<TextView>(R.id.txt_part_name).text = ""
            findViewById<TextView>(R.id.txt_isdefected).text = "검사결과 : 정상"
        }
        else {
            findViewById<TextView>(R.id.txt_part_name).text = getData.result.part
            findViewById<TextView>(R.id.txt_isdefected).text = "검사결과 : " + getData.result.defect_type
        }
        findViewById<TextView>(R.id.txt_tester).text = getData.result.tester
        findViewById<TextView>(R.id.txt_date).text = getData.result.date
        findViewById<TextView>(R.id.txt_stock).text = getData.result.stock.toString()
        findViewById<TextView>(R.id.txt_memo).text = "" + getData.result.memo
    }
}