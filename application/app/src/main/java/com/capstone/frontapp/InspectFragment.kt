package com.capstone.frontapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.user.model.User
import java.net.URI
import java.util.jar.Manifest

class InspectFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_inspect, container, false)

        // 상단바 사용자 이름 설정
        val nameSet = view.findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 계정 관리 화면 이동
        view.findViewById<ImageButton>(R.id.btn_account).setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        // 갤러리 사진 선택 버튼
        view.findViewById<Button>(R.id.btn_gallery).setOnClickListener {
            (activity as UserActivity).selectPhoto()
        }

        // 카메라 사진 촬영 버튼
        view.findViewById<Button>(R.id.btn_shot).setOnClickListener {
            (activity as UserActivity).shootPhoto()
        }

        // 검사하기 버튼
        view.findViewById<Button>(R.id.btn_summit).setOnClickListener {
            (activity as UserActivity).inspect()
        }

        // 사용자 검사 내역 화면 이동
        view.findViewById<Button>(R.id.btn_list).setOnClickListener {
            it.findNavController().navigate(R.id.action_inspectFragment_to_userResultListFragment)
        }

        // 통화 연결 화면 이동
        view.findViewById<Button>(R.id.btn_call).setOnClickListener {
            it.findNavController().navigate(R.id.action_inspectFragment_to_userCallFragment)
        }

        return view
    }

}