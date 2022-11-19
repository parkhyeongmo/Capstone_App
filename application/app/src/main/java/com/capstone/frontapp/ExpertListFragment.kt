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
import androidx.recyclerview.widget.RecyclerView
import com.capstone.frontapp.databinding.FragmentExpertListBinding
import com.capstone.frontapp.databinding.FragmentUserResultListBinding
import retrofit2.Call
import retrofit2.Response

class ExpertListFragment : Fragment() {

    private lateinit var ExpertActivity : ExpertActivity
    private lateinit var RVAdapter : InspectRVAdapter

    private var totalCount: Int = 0
    private var hasNext: Boolean = false
    private var page: Int = 0

    private var first: Boolean = true
    private var part: Int = 0 // 부품 종류 (전체 0, 덕트 1, 선체 2, 선박배관 3, 케이블 4, 보온재 5)
    private var result: Int = 1 // 조치 여부 (미조치 0, 조치 1)
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

//        val items = ArrayList<inspectListItem>()
//
//        for (i: Int in 1..10) {
//            cnt++
//            items.add(inspectListItem(cnt, (cnt + 12).toString(), 1, 1, "2222/22/22"))
//        }
//
//        hasNext = true
//        nextPage()
//        RVAdapter.setList(items)

        // 전체 불량 부품 목록 호출
        if (part == 0) {

            // 불량 부품 목록 호출
            if (result == 1) {
                RetrofitClass.api.getList(UserInfo.jwt.toString(), page = nextPage()).enqueue(object : retrofit2.Callback<inspectList> {
                    override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
                        if (response.body() != null && response.isSuccessful){
                            totalCount = response.body()!!.cnt
                            hasNext = response.body()!!.hasNextPage
                            RVAdapter.setList(response.body()!!.inspectList)
                        }
                        else {
                            Log.i("검사 내역 호출", "실패")
                        }
                    }

                    override fun onFailure(call: Call<inspectList>, t: Throwable) {
                        Log.i("검사 내역 호출", "실패, " + t.message)
                    }
                })
            }

            // 미조치 불량 부품 목록 호출
            else {
                RetrofitClass.api.getList(UserInfo.jwt.toString(), page = nextPage(), result = this.result).enqueue(object : retrofit2.Callback<inspectList> {
                    override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
                        if (response.body() != null && response.isSuccessful){
                            totalCount = response.body()!!.cnt
                            hasNext = response.body()!!.hasNextPage
                            RVAdapter.setList(response.body()!!.inspectList)
                        }
                        else {
                            Log.i("검사 내역 호출", "실패")
                        }
                    }

                    override fun onFailure(call: Call<inspectList>, t: Throwable) {
                        Log.i("검사 내역 호출", "실패, " + t.message)
                    }
                })
            }


        }

        // 불량 부품 종류 선택 목록 호출
        else {
            // 불량 부품 종류 선택 목록 전체 호출
            if (result == 1) {
                RetrofitClass.api.getList(UserInfo.jwt.toString(), page = nextPage(), part = this.part).enqueue(object : retrofit2.Callback<inspectList> {
                    override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
                        if (response.body() != null && response.isSuccessful){
                            totalCount = response.body()!!.cnt
                            hasNext = response.body()!!.hasNextPage
                            RVAdapter.setList(response.body()!!.inspectList)
                        }
                        else {
                            Log.i("검사 내역 호출", "실패")
                        }
                    }

                    override fun onFailure(call: Call<inspectList>, t: Throwable) {
                        Log.i("검사 내역 호출", "실패, " + t.message)
                    }
                })
            }
            // 미조치 불량 부품 종류 선택 목록 호출
            else{
                RetrofitClass.api.getList(UserInfo.jwt.toString(), page = nextPage(), part = this.part, result = this.result).enqueue(object : retrofit2.Callback<inspectList> {
                    override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
                        if (response.body() != null && response.isSuccessful){
                            totalCount = response.body()!!.cnt
                            hasNext = response.body()!!.hasNextPage
                            RVAdapter.setList(response.body()!!.inspectList)
                        }
                        else {
                            Log.i("검사 내역 호출", "실패")
                        }
                    }

                    override fun onFailure(call: Call<inspectList>, t: Throwable) {
                        Log.i("검사 내역 호출", "실패, " + t.message)
                    }
                })
            }
        }

    }

    // 이전 페이지 항목 불러오기
    private fun beforeItems() {

//        val items = ArrayList<inspectListItem>()
//
//        for (i: Int in 1..10) {
//            cnt++
//            items.add(inspectListItem(cnt, cnt.toString(), 1, 1, "2222/22/22"))
//        }
//
//        hasNext = true
//        beforePage()
//        RVAdapter.setList(items)

        // 전체 불량 부품 목록 호출
        if (part == 0) {

            // 불량 부품 목록 호출
            if (result == 1) {
                RetrofitClass.api.getList(UserInfo.jwt.toString(), page = beforePage()).enqueue(object : retrofit2.Callback<inspectList> {
                    override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
                        if (response.body() != null && response.isSuccessful){
                            totalCount = response.body()!!.cnt
                            hasNext = response.body()!!.hasNextPage
                            RVAdapter.setList(response.body()!!.inspectList)
                        }
                        else {
                            Log.i("검사 내역 호출", "실패")
                        }
                    }

                    override fun onFailure(call: Call<inspectList>, t: Throwable) {
                        Log.i("검사 내역 호출", "실패, " + t.message)
                    }
                })
            }

            // 미조치 불량 부품 목록 호출
            else {
                RetrofitClass.api.getList(UserInfo.jwt.toString(), page = beforePage(), result = this.result).enqueue(object : retrofit2.Callback<inspectList> {
                    override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
                        if (response.body() != null && response.isSuccessful){
                            totalCount = response.body()!!.cnt
                            hasNext = response.body()!!.hasNextPage
                            RVAdapter.setList(response.body()!!.inspectList)
                        }
                        else {
                            Log.i("검사 내역 호출", "실패")
                        }
                    }

                    override fun onFailure(call: Call<inspectList>, t: Throwable) {
                        Log.i("검사 내역 호출", "실패, " + t.message)
                    }
                })
            }


        }

        // 불량 부품 종류 선택 목록 호출
        else {
            // 불량 부품 종류 선택 목록 전체 호출
            if (result == 1) {
                RetrofitClass.api.getList(UserInfo.jwt.toString(), page = beforePage(), part = this.part).enqueue(object : retrofit2.Callback<inspectList> {
                    override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
                        if (response.body() != null && response.isSuccessful){
                            totalCount = response.body()!!.cnt
                            hasNext = response.body()!!.hasNextPage
                            RVAdapter.setList(response.body()!!.inspectList)
                        }
                        else {
                            Log.i("검사 내역 호출", "실패")
                        }
                    }

                    override fun onFailure(call: Call<inspectList>, t: Throwable) {
                        Log.i("검사 내역 호출", "실패, " + t.message)
                    }
                })
            }
            // 미조치 불량 부품 종류 선택 목록 호출
            else{
                RetrofitClass.api.getList(UserInfo.jwt.toString(), page = beforePage(), part = this.part, result = this.result).enqueue(object : retrofit2.Callback<inspectList> {
                    override fun onResponse(call: Call<inspectList>, response: Response<inspectList>) {
                        if (response.body() != null && response.isSuccessful){
                            totalCount = response.body()!!.cnt
                            hasNext = response.body()!!.hasNextPage
                            RVAdapter.setList(response.body()!!.inspectList)
                        }
                        else {
                            Log.i("검사 내역 호출", "실패")
                        }
                    }

                    override fun onFailure(call: Call<inspectList>, t: Throwable) {
                        Log.i("검사 내역 호출", "실패, " + t.message)
                    }
                })
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ExpertActivity = context as ExpertActivity

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExpertListBinding.inflate(inflater, container, false)

        // 상단바 사용자 이름 설정
        val nameSet = binding.userName
        nameSet.text = UserInfo.name + "님"

        // 계정 관리 화면 이동
        binding.btnAccount.setOnClickListener {
            val intent = Intent(context, AccountActivity::class.java)
            startActivity(intent)
        }

        // 부품 선택 스피너
        val spinner = binding.spinnerSort
        val sortBy = resources.getStringArray(R.array.expert_sortby)
        val spinnerAdapter = ArrayAdapter(ExpertActivity, android.R.layout.simple_list_item_1, sortBy)
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

        // 미조치 체크박스
        binding.checkFixed.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                result = 0
                page = 0
                nextItems()
                binding.numPage.text = page.toString()
            }
            else {
                result = 1
                page = 0
                nextItems()
                binding.numPage.text = page.toString()
            }
        }

        // 리사이클러 뷰 설정
        RVAdapter = InspectRVAdapter()
        val RV = binding.expertListRecyclerview
        RV.adapter = RVAdapter

        nextItems()

        RVAdapter.itemClick = object : InspectRVAdapter.ItemClick {
            override fun onClick(view: View, position: Int, testId: Int) {
                RetrofitClass.api.getResult(UserInfo.jwt.toString(), testId).enqueue(object : retrofit2.Callback<inspectResult> {
                    override fun onResponse(
                        call: Call<inspectResult>,
                        response: Response<inspectResult>
                    ) {
                        var intent = Intent(ExpertActivity, UserResultActivity::class.java)
                        intent = intent.putExtra("inspection", response.body()!!)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<inspectResult>, t: Throwable) {
                        Toast.makeText(ExpertActivity, "검사 결과 요청 실패", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        // 이전 페이지 버튼
        binding.btnBackPage.setOnClickListener {
            if (page <= 1) {
                Toast.makeText(ExpertActivity, "첫번째 페이지입니다.", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(ExpertActivity, "마지막 페이지입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 재고 관리 화면 이동
        binding.btnStock.setOnClickListener {
            it.findNavController().navigate(R.id.action_expertListFragment_to_expertStockFragment)
        }

        // 번호 등록 화면 이동
        binding.btnRegNum.setOnClickListener {
            it.findNavController().navigate(R.id.action_expertListFragment_to_expertRegNumFragment)
        }

        return binding.root
    }

}