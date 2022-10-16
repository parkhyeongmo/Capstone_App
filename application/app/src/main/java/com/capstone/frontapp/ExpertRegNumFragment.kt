package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController

class ExpertRegNumFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_expert_reg_num, container, false)

        // 계정 관리 화면 이동 버튼
        view.findViewById<Button>(R.id.btn_account).setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        // 전화번호 입력 폼 번호 양식 설정
        view.findViewById<EditText>(R.id.edit_expert_tel).addTextChangedListener(PhoneNumberFormattingTextWatcher())

        // 담당자 검사 내역 화면 이동
        view.findViewById<Button>(R.id.btn_expert_list).setOnClickListener {
            it.findNavController().navigate(R.id.action_expertRegNumFragment_to_expertListFragment)
        }
        // 담당자 재고 관리 화면 이동
        view.findViewById<Button>(R.id.btn_stock).setOnClickListener {
            it.findNavController().navigate(R.id.action_expertRegNumFragment_to_expertStockFragment)
        }

        return view
    }

}