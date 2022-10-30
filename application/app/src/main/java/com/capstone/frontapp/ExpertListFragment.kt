package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController

class ExpertListFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expert_list, container, false)

        // 상단바 사용자 이름 설정
        val nameSet = view.findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 계정 관리 화면 이동
        view.findViewById<Button>(R.id.btn_account).setOnClickListener {

            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)

        }

        view.findViewById<Button>(R.id.btn_stock).setOnClickListener {
            it.findNavController().navigate(R.id.action_expertListFragment_to_expertStockFragment)
        }

        view.findViewById<Button>(R.id.btn_reg_num).setOnClickListener {
            it.findNavController().navigate(R.id.action_expertListFragment_to_expertRegNumFragment)
        }

        return view
    }


}