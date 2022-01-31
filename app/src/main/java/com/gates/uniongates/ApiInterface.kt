package com.gates.uniongates

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST("name")
    fun createEmail(@Body name: String): Call<String>?

}