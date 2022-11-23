package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import retrofit2.Call
import retrofit2.Response

class ExpertRegNumFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expert_reg_num, container, false)

        // 상단바 사용자 이름 설정
        val nameSet = view.findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 계정 관리 화면 이동 버튼
        view.findViewById<ImageButton>(R.id.btn_account).setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }


        // 전화번호 입력 폼 양식 설정
        val phone_num = view.findViewById<EditText>(R.id.edit_expert_tel)
        phone_num.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        // 담당자 번호 등록 버튼
        view.findViewById<Button>(R.id.btn_telnum).setOnClickListener {
            val name: String? = view.findViewById<EditText>(R.id.edit_expert_name).text.toString()
            val hp: String? = view.findViewById<EditText>(R.id.edit_expert_tel).text.toString()

            // 번호 유효성 검사
            if (hp!!.length < 12) {
                Toast.makeText(context as ExpertActivity, "번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            var body = HashMap<String, Any?>()
            body.put("name", name)
            body.put("hp", hp)

            RetrofitClass.api.changeExpertNum(UserInfo.jwt.toString(), body)!!.enqueue(object : retrofit2.Callback<result> {
                override fun onResponse(call: Call<result>, response: Response<result>) {
                    Toast.makeText(context as ExpertActivity, "등록 완료", Toast.LENGTH_SHORT).show()
                    Log.i("담당자 번호 등록", response.body()!!.rst.toString())
                }

                override fun onFailure(call: Call<result>, t: Throwable) {
                    Toast.makeText(context as ExpertActivity, "등록 실패", Toast.LENGTH_SHORT).show()
                }

            })
        }


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