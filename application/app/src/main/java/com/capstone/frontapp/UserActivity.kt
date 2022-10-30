package com.capstone.frontapp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.util.jar.Manifest

class UserActivity : AppCompatActivity() {

    private var selectedUri : Uri? = null
    private var shootedUri : Uri? = null

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

    fun uploadImg() {

        val path = selectedUri?.path.toString()
//        val path = selectedUri.toString().split('.')
//        Log.i("split", path.toString())
        //val file = File(selectedUri.toString())
        Log.i("path", path)
        //Log.i("file", file.path + file.name)

        //Log.i("path", path.toString())
        var requestBody : RequestBody = RequestBody.create(MediaType.parse("image/*"), path)
        var body : MultipartBody.Part = MultipartBody.Part.createFormData("filename","moon.jpg",requestBody)
        Log.i("body", body.body().toString())

//        RetrofitClass.api.test(body)!!.enqueue(object : retrofit2.Callback<result> {
//            override fun onResponse(call: Call<result>, response: Response<result>) {
//                Log.i("img", "success " + response.body()?.code.toString())
//            }
//            override fun onFailure(call: Call<result>, t: Throwable) {
//                Log.i("img", "fail " + t.message.toString())
//            }
//
//        })
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
                val selectedImageURI : Uri? = data?.data

                if(selectedImageURI != null) {
                    val imagePreview = findViewById<ImageView>(R.id.imagePreview)
                    imagePreview.setImageURI(selectedImageURI)
                    selectedUri = selectedImageURI
                    Log.i("성공", "${selectedImageURI}, ${selectedUri}")
                }
                else {
                    Toast.makeText(this, "사진 로드 실패", Toast.LENGTH_SHORT).show()
                }

            }
            // 카메라 사진 촬영 시
            3000 -> {
                val bitmap = data?.extras?.get("data") as Bitmap
                val imagePreview = findViewById<ImageView>(R.id.imagePreview)
                imagePreview.setImageBitmap(bitmap)
            }

            else -> {
                Toast.makeText(this, "잘못된 접근입니다", Toast.LENGTH_SHORT).show()
            }
        }

    }

}
