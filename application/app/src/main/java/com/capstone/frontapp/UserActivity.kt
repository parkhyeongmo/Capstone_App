package com.capstone.frontapp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapRegionDecoder
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.util.jar.Manifest

// Bitmap 파일을 RequestBody 형태로 변형해주는 Class
class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
    override fun contentType(): MediaType? = "image/jpg".toMediaType()

    override fun writeTo(sink: BufferedSink) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
    }
}


class UserActivity : AppCompatActivity() {

    private var selectedUri : Uri? = null
    private var shootedBitmap : Bitmap? = null

    // 권한 요청
    private val requestMultiplePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
        result.forEach {
            if(!it.value) {
                Toast.makeText(applicationContext, "권한 동의 필요", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    
    // 요청할 권한 목록
    private val permissionList = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // 권한 요청 실행
        requestMultiplePermission.launch(permissionList)

        // 선택된 이미지 초기화
        selectedUri = null
        shootedBitmap = null
    }

    // 갤러리에서 사진 선택
    fun selectPhoto() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)
    }
    // 카메라로 사진 촬영
    fun shootPhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 3000)
    }

    // Server로 이미지 업로드
    fun uploadImg() {

        // 파일명과 파일을 저장할 변수
        var fileName : String? = null
        var bitmap : Bitmap? = null

        // 갤러리에서 사진 선택 시
        if (selectedUri != null && shootedBitmap == null) {
            // 파일 이름 저장 및 이미지 Uri를 이용해 비트맵으로 만듦
            val imageUri = selectedUri.toString().split('%')
            fileName = imageUri[1] + ".jpg"
            bitmap = selectedUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(contentResolver, it)
                }
                else {
                    val source = ImageDecoder.createSource(contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
            }
            selectedUri = null
        }
        // 카메라로 사진 촬영 시
        else if (selectedUri == null && shootedBitmap != null) {
            bitmap = shootedBitmap
            fileName = shootedBitmap.toString().split('@')[1] + ".jpg"
            shootedBitmap = null
        }
        // 선택 또는 촬영된 사진이 없을 경우
        else {
            Toast.makeText(this, "선택된 이미지가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // RequestBody 생성
        val bitmapRequestBody = bitmap?.let { BitmapRequestBody(it) }
        val body : MultipartBody.Part? =
            if (bitmapRequestBody == null) null
            else
                MultipartBody.Part.createFormData("filename", fileName, bitmapRequestBody!!)

        // api 호출
        RetrofitClass.api.test(body)!!.enqueue(object : retrofit2.Callback<result> {
            override fun onResponse(call: Call<result>, response: Response<result>) {
                Log.i("img send", "success " + response.body()?.rst.toString())
            }
            override fun onFailure(call: Call<result>, t: Throwable) {
                Log.i("img send", "fail " + t.message.toString())
            }
        })

    }

    // 갤러리에서 선택된 사진 경로 저장 후 이미지뷰에 출력
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_SHORT).show()
            return
        }

        when(requestCode) {
            // 갤러리에서 사진 호출 시
            2000 -> {
                shootedBitmap = null // 이전에 촬영된 사진이 있으면 null로 변경
                val selectedImageURI : Uri? = data?.data
                if(selectedImageURI != null) {
                    val imagePreview = findViewById<ImageView>(R.id.imagePreview)
                    imagePreview.setImageURI(selectedImageURI)
                    selectedUri = selectedImageURI
                }
                else {
                    Toast.makeText(this, "사진 로드 실패", Toast.LENGTH_SHORT).show()
                }

            }
            // 카메라 사진 촬영 시
            3000 -> {
                selectedUri = null // 이전에 선택된 사진이 있으면 null로 변경
                val bitmap = data?.extras?.get("data") as Bitmap
                val imagePreview = findViewById<ImageView>(R.id.imagePreview)
                imagePreview.setImageBitmap(bitmap)
                shootedBitmap = bitmap
            }
            // 접근 오류
            else -> {
                Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
