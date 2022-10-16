package com.capstone.frontapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import java.util.jar.Manifest

class UserActivity : AppCompatActivity() {

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

//    private val readImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        val image = findViewById<ImageView>(R.id.image_upload)
//        image.load(uri)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // 권한 요청 실행
        requestMultiplePermission.launch(permissionList)
    }
}