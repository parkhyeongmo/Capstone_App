package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kakao.sdk.user.model.User

class UserResultListFragment : Fragment() {

    lateinit var UserActivity : UserActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserActivity = context as UserActivity
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

        val sortBy = resources.getStringArray(R.array.user_sortby)
        val spinnerAdapter = ArrayAdapter<String>(UserActivity, android.R.layout.simple_list_item_1, sortBy)
        view.findViewById<Spinner>(R.id.spinner_sort).adapter = spinnerAdapter

        val items = mutableListOf<inspectListItem>()

        items.add(inspectListItem("2022/11/08 11:22:33", "덕트", true, false))
        items.add(inspectListItem("2022/11/08 11:22:33", "선체", false, true))
        items.add(inspectListItem("2022/11/08 11:22:33", "선박배관", true, true))
        items.add(inspectListItem("2022/11/08 11:22:33", "케이블", false, false))
        items.add(inspectListItem("2022/11/08 11:22:33", "덕트", true, false))
        items.add(inspectListItem("2022/11/08 11:22:33", "선체", false, true))
        items.add(inspectListItem("2022/11/08 11:22:33", "선박배관", true, true))
        items.add(inspectListItem("2022/11/08 11:22:33", "케이블", false, false))
        items.add(inspectListItem("2022/11/08 11:22:33", "덕트", true, false))
        items.add(inspectListItem("2022/11/08 11:22:33", "선체", false, true))
        items.add(inspectListItem("2022/11/08 11:22:33", "선박배관", true, true))
        items.add(inspectListItem("2022/11/08 11:22:33", "케이블", false, false))
        items.add(inspectListItem("2022/11/08 11:22:33", "덕트", true, false))
        items.add(inspectListItem("2022/11/08 11:22:33", "선체", false, true))
        items.add(inspectListItem("2022/11/08 11:22:33", "선박배관", true, true))
        items.add(inspectListItem("2022/11/08 11:22:33", "케이블", false, false))

        val inspectRV = view.findViewById<RecyclerView>(R.id.user_list_recyclerview)
        val rvAdapter = InspectRVAdapter(items)

        inspectRV.adapter = rvAdapter

        rvAdapter.itemClick = object : InspectRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(UserActivity, items[position].toString(), Toast.LENGTH_SHORT).show()
            }
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