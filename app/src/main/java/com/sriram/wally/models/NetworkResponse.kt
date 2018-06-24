package com.sriram.wally.models

data class NetworkResponse<T>(
        var status: NetworkStatus,
        var items: ArrayList<T> = arrayListOf()
)