package com.capstone.frontapp

import retrofit2.Call
import retrofit2.http.*

data class loginResponse(
    val id:String,
    val name:String,
    val type:Boolean
)

interface APIInterface {

    // 로그인
    @POST("/auth/login")
    fun logIn(
        @Header("authorization") accessToken:String
    ): Call<loginResponse>

    // 회원가입
    @POST("/auth/sign-up")
    fun signUp(
        @Header("authorization") token: String,
        @Body type: Boolean,
        @Body empnum: Int,
        @Body hp: String
    ): Call<loginResponse>

    // 로그아웃
    @GET("/member/logout")
    fun logOut(
    )

    // 회원탈퇴
    @POST("/member/leave")
    fun unLink(
    )

}