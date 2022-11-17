package com.capstone.frontapp

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.Serializable
import java.util.ArrayList

// 로그인 결과 Response data class
data class loginResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: Int,
    @SerializedName("jwtToken")
    val jwt: String
)

data class result(
    @SerializedName("result")
    val rst: String?
)

// 부품 목록 리스트 Response data class
data class partsList(
    @SerializedName("result")
    val partsList: List<part>
)

// 부품 재고 Response data class
data class part(
    @SerializedName("part_id")
    val part_id: Int,
    @SerializedName("name")
    val part_name: String,
    @SerializedName("stock")
    val stock: Int
)

// 부품 검사 결과 Resonse data class
data class inspectResult(
    @SerializedName("result")
    val result: inspection,
    @SerializedName("imageStr")
    val imgStr: String
) : Serializable

// 부품 검사 결과 상세 내역 Response data class
data class inspection(
    @SerializedName("tester")
    val tester: String,
    @SerializedName("partName")
    val part: String,
    @SerializedName("partStock")
    val stock: Int,
    @SerializedName("isDefected")
    val isdefected: Int,
    @SerializedName("defectedType")
    val defect_type: String?,
    @SerializedName("isFixed")
    val isfixed: Int,
    @SerializedName("testDate")
    val date: String,
    @SerializedName("memo")
    val memo: String,
)

// 검사 내역 Response data class
data class inspectList(
    @SerializedName("result")
    val inspectList: ArrayList<inspectListItem>,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("cnt")
    val cnt: Int
)

// 검사 내역 항목 Response data class
data class inspectListItem(
    @SerializedName("testId")
    val testid: Int,
    @SerializedName("partName")
    val part: String,
    @SerializedName("isDefected")
    val isdefected: Int,
    @SerializedName("isFixed")
    val isfixed: Int,
    @SerializedName("date")
    val date: String
)

interface APIInterface {

    // 로그인
    @POST("/auth/login")
    fun logIn(
        @Header("authorization") accessToken:String
    ): Call<loginResponse>

    // 회원가입
    @Headers("accept: application/json", "content-type: application/json")
    @POST("/auth/sign-up")
    fun signUp(
        @Header("authorization") accessToken: String,
        @Body params: HashMap<String, Any>
    ): Call<loginResponse>

    // 회원탈퇴
    @POST("/auth/leave")
    fun unLink(
        @Header("authorization") jwtToken: String
    ): Call<result>

    // 부품 목록 호출
    @GET("inspection/part/list")
    fun getParts(
        @Header("authorization") jwtToken: String
    ): Call<partsList>

    // 부품 재고 수정
    @PATCH("/admin/stock/{part_id}")
    fun changeStock(
        @Header("authorization") jwtToken: String,
        @Path("part_id") part_id: Int
    )

    // 부품 품질 검사
    @Multipart
    @POST("/inspection/upload")
    fun inspect(
        @Header("authorization") jwtToken: String,
        @Part image: MultipartBody.Part?
    ): Call<inspectResult>

    // 검사 내역 목록
    @GET("/inspection/list")
    fun getList(
        @Header("authorization") jwtToken: String,
        @Query("page") page: Int? = null,
        @Query("part") part: Int? = null,
        @Query("result") result: Int? = null
    ): Call<inspectList>

    // 검사 상세 결과
    @GET("/inspection/list/{testId}")
    fun getResult(
        @Header("authorization") jwtToken: String,
        @Path("testId") testId: Int
    ): Call<inspectResult>

    // 메모 등록
    @POST("/user/memo")
    fun memo(

    )

    // 메모 수정
    @PATCH("/user/memo/{memo_id}")
    fun changeMemo(

    )

    // 메모 삭제
    @DELETE("/user/memo/:memo_id")
    fun deleteMemo(

    )

    // 관리자 불량 조치
    @PATCH("/admin/test/:test_id")
    fun complete(

    )

}