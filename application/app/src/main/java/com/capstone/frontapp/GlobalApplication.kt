package com.capstone.frontapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "5a810df49a17ce172bbe78639ab3f19a")

    }

}