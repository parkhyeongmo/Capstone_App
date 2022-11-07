package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.findNavController
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import javax.security.auth.callback.Callback

class ExpertStockFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expert_stock, container, false)

        // 상단바 사용자 이름 설정
        val nameSet = view.findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 계정 관리 화면 이동
        view.findViewById<ImageButton>(R.id.btn_account).setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        // 부품 목록
        val list = mutableListOf<PartsListViewModel>()

//        // 부품 목록 요청 API
//        RetrofitClass.api.getParts()!!.enqueue(object : retrofit2.Callback<returnPartsList> {
//            override fun onResponse(call: Call<returnPartsList>, response: Response<returnPartsList>) {
//                // 각 부품 별 이름, 재고 목록에 추가
//                for (i in response.body()!!.partsList) {
//                    list.add(PartsListViewModel(i.part_id, i.part_name, i.stock))
//                }
//                // Listview로 출력
//                val listAdapter = PartsListViewAdapter(list)
//                view.findViewById<ListView>(R.id.part_list_view).adapter = listAdapter
//            }
//
//            override fun onFailure(call: Call<returnPartsList>, t: Throwable) {
//                Log.i("부품 목록 호출", "실패")
//            }
//        })

        // 관리자 검사 내역 화면 이동
        view.findViewById<Button>(R.id.btn_expert_list).setOnClickListener {
            it.findNavController().navigate(R.id.action_expertStockFragment_to_expertListFragment)
        }
        // 관리자 번호 등록 화면 이동
        view.findViewById<Button>(R.id.btn_reg_num).setOnClickListener {
            it.findNavController().navigate(R.id.action_expertStockFragment_to_expertRegNumFragment)
        }

        return view
    }

}