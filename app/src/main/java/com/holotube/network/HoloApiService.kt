package com.holotube.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.holotools.app/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface HoloApiService {
    @GET("live?hide_channel_desc=1")
    suspend fun getChannelList(): ChannelList
}

object HoloApi {
    val retrofitService : HoloApiService by lazy {
        retrofit.create(HoloApiService::class.java) }
}