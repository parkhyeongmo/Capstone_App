package com.capstone.frontapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.findViewTreeOnBackPressedDispatcherOwner
import androidx.navigation.findNavController
import retrofit2.Call
import retrofit2.Response

class UserCallFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_call, container, false)

        // 상단바 사용자 이름 설정
        val nameSet = view.findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 담당자 번호 호출
        RetrofitClass.api.getExpertNum().enqueue(object : retrofit2.Callback<expertNum> {
            override fun onResponse(call: Call<expertNum>, response: Response<expertNum>) {
                if (response.isSuccessful) {
                    view.findViewById<TextView>(R.id.expert_name).text = response.body()!!.name
                    view.findViewById<TextView>(R.id.tel_num).text = response.body()!!.hp
                }
                else {
                    Toast.makeText(context as UserActivity, "담당자 번호 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<expertNum>, t: Throwable) {
                Toast.makeText(context as UserActivity, "담당자 번호 불러오기 실패", Toast.LENGTH_SHORT).show()
                Log.i("담당자 번호 호출", t.message.toString())
            }
        })

        // 통화 연결 버튼 이벤트
        view.findViewById<Button>(R.id.btn_call_req).setOnClickListener {
            val tel_num = view.findViewById<TextView>(R.id.tel_num).text.toString()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${tel_num}")
            startActivity(intent)
        }

        // 계정 관리 화면 이동
        view.findViewById<ImageButton>(R.id.btn_account).setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }
        // 부품 검사 화면 이동
        view.findViewById<Button>(R.id.btn_inspect).setOnClickListener {
            it.findNavController().navigate(R.id.action_userCallFragment_to_inspectFragment)
        }
        // 사용자 검사 내역 화면 이동
        view.findViewById<Button>(R.id.btn_list).setOnClickListener {
            it.findNavController().navigate(R.id.action_userCallFragment_to_userResultListFragment)
        }

        return view
    }

}