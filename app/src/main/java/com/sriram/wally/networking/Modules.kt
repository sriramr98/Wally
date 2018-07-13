package com.sriram.wally.networking

import android.content.Context
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.sriram.wally.utils.Logger
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

fun getOkHttpInstance(headerInterceptor: HeaderInterceptor, loggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
    return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .build()
}

fun getLoggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor { message -> Logger.i(message) }

    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}

fun getHeaderInterceptor(): HeaderInterceptor {
    return HeaderInterceptor()
}

fun getCache(context: Context): Cache {
    val cacheFile = File(context.cacheDir, "okhttp-cache")

    return Cache(cacheFile, 100 * 1000 * 1000) // 100 MB Cache // don't judge. We get a lot of images
}

fun getRetrofit(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory, rxAdapter: RxJava2CallAdapterFactory): Retrofit {
    return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(rxAdapter)
            .baseUrl("https://api.unsplash.com/")
            .client(okHttpClient)
            .build()

}

fun getGsonFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
}

fun getRxAdapter(): RxJava2CallAdapterFactory {
    return RxJava2CallAdapterFactory.create()
}

fun getPicassoDownloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
    return OkHttp3Downloader(okHttpClient)
}

fun getPicasso(context: Context, downloader: OkHttp3Downloader): Picasso {
    val availableMemmory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
    val cacheSize = availableMemmory / 8
    return Picasso.Builder(context)
            .downloader(downloader)
            .memoryCache(LruCache(cacheSize))
            .loggingEnabled(true)
            .build()

}

fun getWallyService(retrofit: Retrofit): WallyService {
    return retrofit.create(WallyService::class.java)
}