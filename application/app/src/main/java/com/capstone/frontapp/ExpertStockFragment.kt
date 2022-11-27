package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import javax.security.auth.callback.Callback

class ExpertStockFragment : Fragment() {

    private lateinit var ExpertActivity : ExpertActivity

    // 부품 목록 호출
    private fun getStock(listAdapter: PartsRVAdapter) {
        // 부품 목록 요청 API
        RetrofitClass.api.getParts(UserInfo.jwt.toString())!!.enqueue(object : retrofit2.Callback<partsList> {
            override fun onResponse(call: Call<partsList>, response: Response<partsList>) {
                // 각 부품 별 이름, 재고 목록에 추가
                listAdapter.setList(response.body()!!.partsList)
            }
            override fun onFailure(call: Call<partsList>, t: Throwable) {
                Toast.makeText(ExpertActivity, "부품 목록 호출 실패", Toast.LENGTH_SHORT).show()
                Log.i("부품 목록 호출", "실패")
            }
        })
    }

    // 재고 변경
    private fun manageStock(part_id: Int, body: HashMap<String, Any>) {
        RetrofitClass.api.changeStock(UserInfo.jwt.toString(), part_id = part_id, params = body).enqueue(object : retrofit2.Callback<result> {
            override fun onResponse(
                call: Call<result>,
                response: Response<result>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context as ExpertActivity, "재고 변경 완료", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context as ExpertActivity, "재고 변경 실패", Toast.LENGTH_SHORT).show()
                    Log.i("재고 변경 실패", response.code().toString())
                }
            }

            override fun onFailure(call: Call<result>, t: Throwable) {
                Toast.makeText(context as ExpertActivity, "재고 변경 실패", Toast.LENGTH_SHORT).show()
                Log.i("재고 변경 실패", t.message.toString())
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ExpertActivity = context as ExpertActivity
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

        // Listview에 Adapter 연결
        val listView = view.findViewById<RecyclerView>(R.id.part_RV)
        val listAdapter = PartsRVAdapter()
        listView.adapter = listAdapter

        listAdapter.itemClick = object : PartsRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int, part_id: Int) {
                // 부품 재고 변경 다이얼로그 호출
                val builder = AlertDialog.Builder(ExpertActivity)
                val dialogView = layoutInflater.inflate(R.layout.stock_dialog, null)
                val dialogStock = dialogView.findViewById<EditText>(R.id.edit_stock)

                builder.setView(dialogView)
                    .setPositiveButton("변경") {dialogInterface, i ->
                        // 재고 변경 호출
                        val body = HashMap<String, Any>()
                        body.put("stock", dialogStock.text.toString().toInt())
                        manageStock(part_id, body)

                        // 부품 목록 재호출
                        getStock(listAdapter)
                    }
                    .setNegativeButton("취소") {dialogInterface, i ->

                    }.show()
            }
        }

        // 부품 목록 요청 API
        getStock(listAdapter)

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