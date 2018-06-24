package com.sriram.wally.networking

import com.sriram.wally.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
                .addHeader("Accept-Version", "v1")
                .addHeader("Authorization", "Client-ID ${BuildConfig.UNSPLASH_ACCESS_KEY}")
                .build()
        return chain.proceed(newRequest)
    }
}