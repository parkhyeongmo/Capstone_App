package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class ExpertListFragment : Fragment() {

    lateinit var ExpertActivity : ExpertActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ExpertActivity = context as ExpertActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expert_list, container, false)

        // 상단바 사용자 이름 설정
        val nameSet = view.findViewById<TextView>(R.id.user_name)
        nameSet.text = UserInfo.name + "님"

        // 계정 관리 화면 이동
        view.findViewById<ImageButton>(R.id.btn_account).setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        val sortBy = resources.getStringArray(R.array.expert_sortby)
        val spinnerAdapter = ArrayAdapter<String>(ExpertActivity, android.R.layout.simple_list_item_1, sortBy)
        view.findViewById<Spinner>(R.id.spinner_sort).adapter = spinnerAdapter

        val items = mutableListOf<inspectListItem>()

        items.add(inspectListItem(1, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(2, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(3, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(4, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(5, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(6, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(7, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(8, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(9, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(10, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(11, "덕트", true, false, "2022/11/08 11:22:33"))
        items.add(inspectListItem(12, "덕트", true, false, "2022/11/08 11:22:33"))

        val inspectRV = view.findViewById<RecyclerView>(R.id.expert_list_recyclerview)
        val rvAdapter = InspectRVAdapter(items)

        inspectRV.adapter = rvAdapter

        view.findViewById<Button>(R.id.btn_stock).setOnClickListener {
            it.findNavController().navigate(R.id.action_expertListFragment_to_expertStockFragment)
        }

        view.findViewById<Button>(R.id.btn_reg_num).setOnClickListener {
            it.findNavController().navigate(R.id.action_expertListFragment_to_expertRegNumFragment)
        }

        return view
    }

}