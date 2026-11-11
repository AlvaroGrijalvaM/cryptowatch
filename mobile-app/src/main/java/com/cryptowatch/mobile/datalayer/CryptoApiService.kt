package com.cryptowatch.mobile.datalayer

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class CryptoMarketData(
    val id: String,
    val symbol: String,
    val name: String,
    @Json(name = "current_price") val currentPrice: Double,
    @Json(name = "price_change_percentage_24h") val priceChangePercentage24h: Double?
)

interface CryptoApiService {
    @GET("simple/price")
    suspend fun getCurrentPrices(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String = "usd",
        @Query("include_24hr_change") includeChange: Boolean = false
    ): Map<String, Map<String, Double>>

    @GET("coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false
    ): List<CryptoMarketData>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("days") days: Int = 30
    ): Map<String, Any>
}
