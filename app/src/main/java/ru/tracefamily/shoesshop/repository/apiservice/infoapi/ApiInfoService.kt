package ru.tracefamily.shoesshop.repository.apiservice.infoapi

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.CardInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.CommonStocksInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.ImageInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.StocksInfo

interface ApiInfoService {

    @GET("hs/info_api/cards/{barcode}")
    suspend fun getCard(
        @Header("Authorization") credentials: String,
        @Path("barcode") barcode: String
    ): Response<CardInfo>

    @GET("hs/info_api/cards/image/{barcode}")
    suspend fun getBase64Image(
        @Header("Authorization") credentials: String,
        @Path("barcode") barcode: String
    ): Response<ImageInfo>

    @GET("hs/info_api/cards/stock/{barcode}")
    suspend fun getStocks(
        @Header("Authorization") credentials: String,
        @Path("barcode") barcode: String
    ): Response<StocksInfo>

    @GET("hs/info_api/cards/stock/search/{barcode}")
    suspend fun getCommonStocks(
        @Header("Authorization") credentials: String,
        @Path("barcode") barcode: String
    ): Response<CommonStocksInfo>

}