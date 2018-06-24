package com.sriram.wally.di

import com.sriram.wally.adapters.PhotoListAdapter
import com.sriram.wally.networking.*
import com.sriram.wally.ui.home.PhotosListViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext

val modules = applicationContext {

    // networking modules
    bean { getOkHttpInstance(get(), get(), get()) }
    bean { getLoggingInterceptor() }
    bean { getHeaderInterceptor() }
    bean { getCache(androidApplication()) }
    bean { getGsonFactory() }
    bean { getRxAdapter() }
    factory { getRetrofit(get(), get(), get()) }
    factory { getWallyService(get()) }
    bean { getPicassoDownloader(get()) }
    bean { getPicasso(androidApplication(), get()) }
    bean { NetworkRepo(get()) }

    /**
     * Modules relating to PhotoListFragment
     */

    viewModel { PhotosListViewModel(get()) }
    factory { PhotoListAdapter(androidApplication(), get()) }


}