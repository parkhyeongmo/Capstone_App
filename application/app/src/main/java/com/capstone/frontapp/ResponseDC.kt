package com.capstone.frontapp

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

// 로그인 결과 Response data class
data class loginResponse(

    @SerializedName("id")
    val id:String,
    @SerializedName("name")
    val name:String,
    @SerializedName("type")
    val type:Int
)

data class signUp(
    @SerializedName("hello")
    val code:Int? = 0
)

interface APIInterface {

    @GET("/auth/hello")
    fun getHello(
        @Body hello:String
    ): Call<signUp>

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
    ): Call<signUp>

    // 로그아웃
    @GET("/member/logout")
    fun logOut(
    )

    // 회원탈퇴
    @POST("/member/leave")
    fun unLink(
    )

    // 부품 품질 검사
    @POST("/test")
    fun inspect(

    )

    // 검사 내역 목록
    @GET("/test/list?sortby=")
    fun getList(

    )

    // 검사 상세 결과
    @GET("/test/:test_id")
    fun getResult(

    )

    // 메모 등록
    @POST("/user/memo")
    fun memo(

    )

    // 메모 수정
    @PATCH("/user/memo/:memo_id")
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

    // 부품 재고 수정
    @PATCH("/admin/stock/:part_id")
    fun changeStock(

    )


}