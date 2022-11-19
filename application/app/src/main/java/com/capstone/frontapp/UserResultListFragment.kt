package com.capstone.frontapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.frontapp.databinding.FragmentUserResultListBinding
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Response
import kotlin.math.log

class UserResultListFragment : Fragment() {

    private lateinit var UserActivity : UserActivity
    private lateinit var RVAdapter : InspectRVAdapter

    private var totalCount: Int = 0
    private var hasNext: Boolean = false
    private var page: Int = 0

    private var first: Boolean = true
    private var part: Int = 0 // 부품 종류 (전체 0, 덕트 1, 선체 2, 선박배관 3, 케이블 4, 보온재 5)
    var cnt: Int = 0

    private fun nextPage(): Int {
        page++
        return page
    }

    private fun beforePage(): Int {
        page--
        return page
    }

    // 다음 페이지 항목 불러오기
    private fun nextItems() {

        val items = ArrayList<inspectListItem>()

        for (i: Int in 1..10) {
            cnt++
            items.add(inspectListItem(cnt, (cnt + 12).toString(), 1, 1, "2222/22/22"))
        }

        hasNext = true
        nextPage()
        RVAdapter.setList(items)

//        // 전체 부품 목록 호출
//        if (part == 0) {
//            RetrofitClass.api.getList(UserInfo.jwt.toString(), page = nextPage()).enqueue(object : retrofit2.Callback<inspectList> {
//                override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
//                    if (response.body() != null && response.isSuccessful){
//                        totalCount = response.body()!!.cnt
//                        hasNext = response.body()!!.hasNextPage
//                        RVAdapter.setList(response.body()!!.inspectList)
//                    }
//                    else {
//                        Log.i("검사 내역 호출", "실패")
//                    }
//                }
//
//                override fun onFailure(call: Call<inspectList>, t: Throwable) {
//                    Log.i("검사 내역 호출", "실패, " + t.message)
//                }
//            })
//        }
//
//        // 선택 부품 목록 호출
//        else {
//            RetrofitClass.api.getList(UserInfo.jwt.toString(), page = nextPage(), part = this.part).enqueue(object : retrofit2.Callback<inspectList> {
//                override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
//                    if (response.body() != null && response.isSuccessful){
//                        totalCount = response.body()!!.cnt
//                        hasNext = response.body()!!.hasNextPage
//                        RVAdapter.setList(response.body()!!.inspectList)
//                    }
//                    else {
//                        Log.i("검사 내역 호출", "실패")
//                    }
//                }
//
//                override fun onFailure(call: Call<inspectList>, t: Throwable) {
//                    Log.i("검사 내역 호출", "실패, " + t.message)
//                }
//            })
//        }

    }

    // 이전 페이지 항목 불러오기
    private fun beforeItems() {

        val items = ArrayList<inspectListItem>()

        for (i: Int in 1..10) {
            cnt++
            items.add(inspectListItem(cnt, cnt.toString(), 1, 1, "2222/22/22"))
        }

        hasNext = true
        beforePage()
        RVAdapter.setList(items)

//        // 전체 부품 목록 호출
//        if (part == 0) {
//            RetrofitClass.api.getList(UserInfo.jwt.toString(), page = beforePage()).enqueue(object : retrofit2.Callback<inspectList> {
//                override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
//                    if (response.body() != null && response.isSuccessful){
//                        totalCount = response.body()!!.cnt
//                        hasNext = response.body()!!.hasNextPage
//                        RVAdapter.setList(response.body()!!.inspectList)
//                    }
//                    else {
//                        Log.i("검사 내역 호출", "실패")
//                    }
//                }
//
//                override fun onFailure(call: Call<inspectList>, t: Throwable) {
//                    Log.i("검사 내역 호출", "실패, " + t.message)
//                }
//            })
//        }
//
//        // 선택 부품 목록 호출
//        else {
//            RetrofitClass.api.getList(UserInfo.jwt.toString(), page = beforePage(), part = this.part).enqueue(object : retrofit2.Callback<inspectList> {
//                override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
//                    if (response.body() != null && response.isSuccessful){
//                        totalCount = response.body()!!.cnt
//                        hasNext = response.body()!!.hasNextPage
//                        RVAdapter.setList(response.body()!!.inspectList)
//                    }
//                    else {
//                        Log.i("검사 내역 호출", "실패")
//                    }
//                }
//
//                override fun onFailure(call: Call<inspectList>, t: Throwable) {
//                    Log.i("검사 내역 호출", "실패, " + t.message)
//                }
//            })
//        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserActivity = context as UserActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentUserResultListBinding.inflate(inflater, container, false)

        // 상단바 사용자 이름 설정
        binding.userName.text = UserInfo.name + "님"

        // 계정 관리 화면 이동
        binding.btnAccount.setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        // 부품 선택 스피너
        val spinner = binding.spinnerSort
        val sortBy = resources.getStringArray(R.array.user_sortby)
        val spinnerAdapter = ArrayAdapter<String>(UserActivity, android.R.layout.simple_list_item_1, sortBy)
        spinner.adapter = spinnerAdapter

        // 부품 선택 스피너 이벤트
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                part = p2
                if (!first) {
                    page = 0
                    nextItems()
                    binding.numPage.text = page.toString()
                }
                else {
                    first = false
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {                
            }
        }

        // 리사이클러 뷰 설정
        RVAdapter = InspectRVAdapter()
        val RV = binding.userListRecyclerview
        RV.adapter = RVAdapter

        RVAdapter.itemClick = object : InspectRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int, testId: Int) {
                // 인자로 들어오는 testId로 검사 상세 내역 호출
            }
        }

        nextItems()

        // 이전 페이지 버튼
        binding.btnBackPage.setOnClickListener {
            if (page <= 1) {
                Toast.makeText(UserActivity, "첫번째 페이지입니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                beforeItems()
                binding.numPage.text = page.toString()
            }
        }

        // 다음 페이지 버튼
        binding.btnForwardPage.setOnClickListener {
            if (hasNext) {
                nextItems()
                binding.numPage.text = page.toString()
            }
            else {
                Toast.makeText(UserActivity, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 부품 검사 화면 이동
        binding.btnInspect.setOnClickListener {
            it.findNavController().navigate(R.id.action_userResultListFragment_to_inspectFragment)
        }
        // 통화 연결 화면 이동
        binding.btnCall.setOnClickListener {
            it.findNavController().navigate(R.id.action_userResultListFragment_to_userCallFragment)
        }

        return binding.root

    }

}