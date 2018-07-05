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
        // do not add the headers for any request to amazom aws s3. It returns a 400 error.
        if (!request.url().toString().contains("amazonaws")) {
            newRequest.addHeader("Accept-Version", "v1")
                    .addHeader("Authorization", "Client-ID ${BuildConfig.UNSPLASH_ACCESS_KEY}")
        }

        return chain.proceed(newRequest.build())

    }
}