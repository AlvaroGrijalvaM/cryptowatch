package com.cryptowatch.mobile.datalayer

import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import java.util.*

interface CryptoApiService {
    @GET("simple/price")
    suspend fun getCurrentPrices(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String,
        @Query("include_24hr_change") includeChange: Boolean = false
    ): Map<String, Map<String, Double>>

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkle") sparkle: Boolean = false
    ): List<CryptoMarketData>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("days") days: Int = 30
    ): List<ChartPoint>

    data class ChartPoint(
        val time: Long,
        val price: Double
    )
}