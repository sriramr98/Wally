package com.sriram.wally.models

data class NetworkResponse<T>(
        var status: NetworkStatus,
        var items: ArrayList<T> = arrayListOf()
)

// this to be used when the response is a single object instead of a list of objects
data class NetworkSingular<T>(
        var status: NetworkStatus,
        var item: T? = null
)