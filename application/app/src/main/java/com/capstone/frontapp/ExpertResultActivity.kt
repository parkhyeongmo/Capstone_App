package com.capstone.frontapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Response

class ExpertResultActivity : AppCompatActivity() {

    private var result: inspectResult? = null

    private fun getResult(testId: Int) {
        RetrofitClass.api.getResult(UserInfo.jwt.toString(), testId).enqueue(object : retrofit2.Callback<inspectResult> {
            override fun onResponse(
                call: Call<inspectResult>,
                response: Response<inspectResult>
            ) {
                if (response.body() != null) {
                    // 검사 결과 저장
                    result = response.body()

                    // 인코딩된 이미지 디코딩
                    val encodeByte = Base64.decode(response.body()!!.imgStr, Base64.DEFAULT)
                    val img = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)

                    // 검사 결과 화면에 반영
                    findViewById<ImageView>(R.id.image_result).setImageBitmap(img)

                    // 정상 판정 시 부품 명, 불량 사유, 재고
                    if (response.body()!!.result.isdefected == 0) {
                        if (response.body()!!.result.part == null) {
                            findViewById<TextView>(R.id.txt_part_name).text = response.body()!!.result.defect_type
                            findViewById<TextView>(R.id.txt_stock).text = "검출 실패"
                        }
                        else {
                            findViewById<TextView>(R.id.txt_part_name).text = response.body()!!.result.part
                            findViewById<TextView>(R.id.txt_stock).text = "" + response.body()!!.result.stock.toString()
                        }
                        findViewById<TextView>(R.id.txt_isdefected).text = "" + response.body()!!.result.defect_type
                    }

                    // 불량 판정 시 부품 명, 불량 사유, 재고
                    else {
                        findViewById<TextView>(R.id.txt_part_name).text = response.body()!!.result.part
                        findViewById<TextView>(R.id.txt_isdefected).text = response.body()!!.result.defect_type
                        findViewById<TextView>(R.id.txt_stock).text = "" + response.body()!!.result.stock.toString()

                        if (response.body()!!.result.isfixed == 1) {
                            findViewById<TextView>(R.id.txt_isdefected).setTextColor(Color.BLUE)
                            findViewById<Button>(R.id.btn_fix).isEnabled = false // 조치 완료 후 버튼 비활성화
                            findViewById<Button>(R.id.btn_fix).setBackgroundResource(R.drawable.btn_nonactive_background)
                        }
                    }

                    // 검사자, 일자
                    findViewById<TextView>(R.id.txt_tester).text = "" + response.body()!!.result.tester
                    findViewById<TextView>(R.id.txt_date).text = "" + response.body()!!.result.date

                    // 메모
                    if (response.body()!!.result.memo == null) {
                        findViewById<TextView>(R.id.txt_memo).text = "메모 미등록"
                    }
                    else {
                        findViewById<TextView>(R.id.txt_memo).text = "" + response.body()!!.result.memo
                    }
                }

                else {
                    Toast.makeText(this@ExpertResultActivity, "검사 결과 요청 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<inspectResult>, t: Throwable) {
                Toast.makeText(this@ExpertResultActivity, "검사 결과 요청 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 검사 결과 공유
    private fun shareResult() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Test ID: ${result!!.result.testId}\n검사일자: ${result!!.result.date}\n부품명: ${result!!.result.part}\n" +
                    "불량 유형: ${result!!.result.defect_type}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "결과 공유하기")
        startActivity(shareIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expert_result)

        // 출력할 검사 결과 받아오기
        val testId = intent.getIntExtra("testId", -1)
        Log.i("수신", testId.toString())

        // 뒤로가기 버튼
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener{
            onBackPressed()
        }

        // 사용자명 표기
        findViewById<TextView>(R.id.user_name).text = UserInfo.name + "님"

        // 부품 검사 결과 API 호출
        getResult(testId)

        // 결과 공유 버튼
        findViewById<ImageButton>(R.id.btn_share).setOnClickListener {
            shareResult()
        }

        val btn_fix = findViewById<Button>(R.id.btn_fix)

        // 조치 처리 버튼
        btn_fix.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.fix_dialog, null)

            builder.setView(dialogView)
                .setPositiveButton("완료") { dialogInterface, i ->
                    RetrofitClass.api.fix(UserInfo.jwt.toString(), test_id = testId).enqueue(object : retrofit2.Callback<result> {
                        override fun onResponse(call: Call<result>, response: Response<result>) {
                            btn_fix.isEnabled = false
                            Toast.makeText(this@ExpertResultActivity,  "조치 처리 완료", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<result>, t: Throwable) {
                            Toast.makeText(this@ExpertResultActivity,  "조치 처리 실패", Toast.LENGTH_SHORT).show()
                        }
                    })
                    btn_fix.isEnabled = false // 조치 완료 후 버튼 비활성화
                    btn_fix.setBackgroundResource(R.drawable.btn_nonactive_background)
                }
                .setNegativeButton("취소") { dialogInterface, i ->

                }.show()
        }

    }
}