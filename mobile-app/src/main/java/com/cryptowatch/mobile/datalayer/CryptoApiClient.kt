package com.cryptowatch.mobile.datalayer

import retrofit2.Retrofit
import retrofit.converter.scalars.ScalarsConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.KotlinJsonAdapterFactory

object CryptoApiClient {
    private const val BASE_URL = "https://api.coingecko.com/api/v3/"
    private lateinit var retrofit: Retrofit
    private val apiService: CryptoApiService

    init {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        apiService = retrofit.create(CryptoApiService::class.java)
    }

    fun getApiService(): CryptoApiService = apiService
}