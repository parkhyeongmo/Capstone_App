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
    val partsList: ArrayList<part>
)

// 부품 재고 Response data class
data class part(
    @SerializedName("name")
    val part_name: String,
    @SerializedName("stock")
    val stock: Int,
    @SerializedName("member_id")
    val member_id: Int,
    @SerializedName("part_id")
    val part_id: Int
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
    val defect_type: String,
    @SerializedName("isFixed")
    val isfixed: Int?,
    @SerializedName("date")
    val date: String,
    @SerializedName("memo")
    val memo: String?
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
    val isfixed: Int?,
    @SerializedName("date")
    val date: String
)

// 담당자 번호 Response data class
data class expertNum(
    @SerializedName("name")
    val name: String,
    @SerializedName("hp")
    val hp: String
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
    @POST("/memo")
    fun postMemo(
        @Header("authorization") jwtToken: String,
        @Body params: HashMap<String, Any>
    ): Call<result>

    // 메모 수정
    @PUT("/memo/{test_id}")
    fun changeMemo(
        @Header("authorization") jwtToken: String,
        @Path("test_id") testId: Int,
        @Body params: HashMap<String, Any>
    ): Call<result>

    // 메모 삭제
    @DELETE("/memo/{test_id}")
    fun deleteMemo(
        @Header("authorization") jwtToken: String,
        @Path("test_id") testId: Int
    ): Call<result>

    // 담당자 번호 호출
    @GET("/engineer")
    fun getExpertNum(

    ): Call<expertNum>

    // 담당자 번호 등록
    @POST("/engineer")
    fun postExpertNum(
        @Header("authorization") jwtToken: String,
        @Body params: HashMap<String, Any?>
    ): Call<expertNum>

    // 담당자 번호 수정
    @PUT("/engineer")
    fun changeExpertNum(
        @Header("authorization") jwtToken: String,
        @Body params: HashMap<String, Any?>
    ): Call<result>

    // 담당자 번호 삭제
    @DELETE("/engineer")
    fun deleteExpertNum(
        @Header("authorization") jwtToken: String
    ): Call<result>

    // 부품 목록 호출
    @GET("/part/getallparts")
    fun getParts(
        @Header("authorization") jwtToken: String
    ): Call<partsList>

    // 부품 재고 수정
    @PUT("/part/{part_id}")
    fun changeStock(
        @Header("authorization") jwtToken: String,
        @Path("part_id") part_id: Int,
        @Body params: HashMap<String, Any>
    ): Call<result>

    // 불량 부품 조치 처리
    @PUT("/fix/{test_id}")
    fun fix(
        @Header("authorization") jwtToken: String,
        @Path("test_id") test_id: Int
    ): Call<result>

}