package com.capstone.frontapp

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Response

class ExpertResultActivity : AppCompatActivity() {

    private fun getResult(testId: Int) {
        RetrofitClass.api.getResult(UserInfo.jwt.toString(), testId).enqueue(object : retrofit2.Callback<inspectResult> {
            override fun onResponse(
                call: Call<inspectResult>,
                response: Response<inspectResult>
            ) {
                if (response.body() != null) {
                    // 인코딩된 이미지 디코딩
                    val encodeByte = Base64.decode(response.body()!!.imgStr, Base64.DEFAULT)
                    val img = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)

                    // 검사 결과 화면에 반영
                    findViewById<ImageView>(R.id.image_result).setImageBitmap(img)

                    if (response.body()!!.result.isdefected == 0) {
                        findViewById<TextView>(R.id.txt_part_name).text = " "
                        findViewById<TextView>(R.id.txt_isdefected).text = "검사결과 : 정상"
                        findViewById<TextView>(R.id.txt_stock).text = "0"
                    }
                    else {
                        findViewById<TextView>(R.id.txt_part_name).text = response.body()!!.result.part
                        findViewById<TextView>(R.id.txt_isdefected).text = "검사결과 : " + response.body()!!.result.defect_type
                        findViewById<TextView>(R.id.txt_stock).text = "" + response.body()!!.result.stock.toString()
                    }
                    findViewById<TextView>(R.id.txt_tester).text = "" + response.body()!!.result.tester
                    findViewById<TextView>(R.id.txt_date).text = "" + response.body()!!.result.date
                    findViewById<TextView>(R.id.txt_memo).text = "" + response.body()!!.result.memo
                }
            }

            override fun onFailure(call: Call<inspectResult>, t: Throwable) {
                Toast.makeText(this@ExpertResultActivity, "검사 결과 요청 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expert_result)

        // 출력할 검사 결과 받아오기
        val testId = intent.getIntExtra("testId", -1)

        // 뒤로가기 버튼
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener{
            onBackPressed()
        }

        // 사용자명 표기
        findViewById<TextView>(R.id.user_name).text = UserInfo.name + "님"

        // 부품 검사 결과 API 호출
        getResult(testId)

        // 조치 처리 버튼
        findViewById<Button>(R.id.btn_fix).setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.fix_dialog, null)

            builder.setView(dialogView)
                .setPositiveButton("완료") { dialogInterface, i ->
                    RetrofitClass.api.fix(UserInfo.jwt.toString(), test_id = testId).enqueue(object : retrofit2.Callback<result> {
                        override fun onResponse(call: Call<result>, response: Response<result>) {
                            Toast.makeText(this@ExpertResultActivity,  "조치 처리 완료", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<result>, t: Throwable) {
                            Toast.makeText(this@ExpertResultActivity,  "조치 처리 실패", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                .setNegativeButton("취소") { dialogInterface, i ->

                }.show()
        }

    }
}