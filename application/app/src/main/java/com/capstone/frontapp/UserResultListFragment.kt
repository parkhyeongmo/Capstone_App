package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController

class UserResultListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_user_result_list, container, false)

        // 상단바 사용자 이름 설정
        val nameSet = view.findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 계정 관리 화면 이동
        view.findViewById<ImageButton>(R.id.btn_account).setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }
        // 부품 검사 화면 이동
        view.findViewById<Button>(R.id.btn_inspect).setOnClickListener {
            it.findNavController().navigate(R.id.action_userResultListFragment_to_inspectFragment)
        }
        // 통화 연결 화면 이동
        view.findViewById<Button>(R.id.btn_call).setOnClickListener {
            it.findNavController().navigate(R.id.action_userResultListFragment_to_userCallFragment)
        }

        return view

    }

}