package com.capstone.frontapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.kakao.sdk.user.model.User
import okhttp3.MultipartBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Response

class UserResultActivity : AppCompatActivity() {

    private var testId : Int = -1
    private var result : inspectResult? = null

    @RequiresApi(Build.VERSION_CODES.N)
    private fun inspect(bitmap: Bitmap) {
        // 파일명 생성
        val fileName = RandomFileName() + ".jpg"
        // RequestBody 생성
        val bitmapRequestBody = bitmap?.let { BitmapRequestBody(it) }
        val body : MultipartBody.Part? =
            if (bitmapRequestBody == null) null
            else
                MultipartBody.Part.createFormData("filename", fileName, bitmapRequestBody!!)

        // api 호출
        RetrofitClass.api.inspect(UserInfo.jwt.toString(), body)!!.enqueue(object : retrofit2.Callback<inspectResult> {
            override fun onResponse(call: Call<inspectResult>, response: Response<inspectResult>) {
                Log.i("반환", response.body().toString())
                if(response.body() != null) {
                    Log.i("반환 성공", response.body().toString())
                    // testId 저장
                    testId = response.body()!!.result.testId

                    // 검사 결과 저장
                    result = response.body()

                    // 인코딩된 이미지 디코딩
                    val encodeByte = Base64.decode(response.body()!!.imgStr, Base64.DEFAULT)
                    val img = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)

                    // 검사 결과 화면에 반영
                    findViewById<ImageView>(R.id.image_result).setImageBitmap(img)

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
                    else {
                        findViewById<TextView>(R.id.txt_part_name).text = response.body()!!.result.part
                        findViewById<TextView>(R.id.txt_isdefected).text = response.body()!!.result.defect_type
                        findViewById<TextView>(R.id.txt_stock).text = "" + response.body()!!.result.stock.toString()
                    }
                    findViewById<TextView>(R.id.txt_tester).text = "" + response.body()!!.result.tester
                    findViewById<TextView>(R.id.txt_date).text = "" + response.body()!!.result.date

                    if (response.body()!!.result.memo == null) {
                        findViewById<TextView>(R.id.txt_memo).text = "메모 미등록"
                    }
                    else {
                        findViewById<TextView>(R.id.txt_memo).text = "" + response.body()!!.result.memo
                    }
                }
                else {
                    Toast.makeText(this@UserResultActivity, "부품 검사 실패", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<inspectResult>, t: Throwable) {
                Log.i("부품 검사", "실패 " + t.message)
                Toast.makeText(this@UserResultActivity, "부품 검사 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

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
                    Toast.makeText(this@UserResultActivity, "검사 결과 요청 실패", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<inspectResult>, t: Throwable) {
                Toast.makeText(this@UserResultActivity, "검사 결과 요청 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 이미지 파일명 랜덤 생성
    @RequiresApi(Build.VERSION_CODES.N)
    private fun RandomFileName() : String {
        val fineName : String = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        val num : Int = (Math.random() * 100000).toInt()
        return fineName + num.toString()
    }

    // 검사 결과 공유
    private fun shareResult() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Test ID: ${testId}\n검사일자: ${result!!.result.date}\n부품명: ${result!!.result.part}\n" +
                    "불량 유형: ${result!!.result.defect_type}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "결과 공유하기")
        startActivity(shareIntent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_result)

        // 뒤로가기 버튼
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener{
            onBackPressed()
        }

        // 사용자명 표기
        findViewById<TextView>(R.id.user_name).text = UserInfo.name + "님"

        // 검사할 이미지 받아오기
        val getImage: ByteArray? = intent.getByteArrayExtra("image")
        testId = intent.getIntExtra("testId", -1)

        if (getImage != null) {
            val image = BitmapFactory.decodeByteArray(getImage, 0, getImage!!.size)
            // 부품 검사 API 호출
            inspect(image)
            Log.i("이미지 진입", "ddd")
        }

        else if (testId != -1) {
            // 상세 결과 API
            getResult(testId)
            Log.i("목록 진입", testId.toString())
        }

        // 공유 버튼
        findViewById<ImageButton>(R.id.btn_share).setOnClickListener {
            shareResult()
        }

        // 메모 버튼
        findViewById<Button>(R.id.btn_memo).setOnClickListener {

            if (testId == -1) {
                return@setOnClickListener
            }

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.memo_dialog, null)
            val dialogMemo = dialogView.findViewById<EditText>(R.id.edit_memo)

            builder.setView(dialogView)
                .setPositiveButton("등록") { dialogInterface, i ->

                    if (result!!.result.memo == null) {
                        val body = HashMap<String, Any>()
                        body.put("content", dialogMemo.text.toString())
                        body.put("test_id", testId)

                        // 메모 등록 API 호출
                        RetrofitClass.api.postMemo(UserInfo.jwt.toString(), body)!!.enqueue(object : retrofit2.Callback<result> {
                            override fun onResponse(call: Call<result>, response: Response<result>) {
                                if (response.isSuccessful) {
                                    findViewById<TextView>(R.id.edit_memo).text = dialogMemo.text.toString()
                                    Toast.makeText(this@UserResultActivity, "메모 등록 성공", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<result>, t: Throwable) {
                                Toast.makeText(this@UserResultActivity, "메모 등록 실패", Toast.LENGTH_SHORT).show()
                                Log.i("메모 등록 실패", t.message.toString())
                            }
                        })
                    }

                    else {
                        val body = HashMap<String, Any>()
                        body.put("content", dialogMemo.text.toString())

                        // 이미 등록된 메모가 있는 경우 메모 수정 API 호출
                        RetrofitClass.api.changeMemo(UserInfo.jwt.toString(), testId = testId, body)!!.enqueue(object : retrofit2.Callback<result> {
                            override fun onResponse(
                                call: Call<result>,
                                response: Response<result>
                            ) {
                                if (response.isSuccessful) {
                                    findViewById<TextView>(R.id.edit_memo).text = dialogMemo.text.toString()
                                    Toast.makeText(this@UserResultActivity, "메모 등록 성공", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<result>, t: Throwable) {
                                Toast.makeText(this@UserResultActivity, "메모 등록 실패", Toast.LENGTH_SHORT).show()
                                Log.i("메모 등록 실패", t.message.toString())
                            }
                        })
                    }

                }
                .setNegativeButton("취소") { dialogInterface, i ->

                }.show()
        }
    }

}