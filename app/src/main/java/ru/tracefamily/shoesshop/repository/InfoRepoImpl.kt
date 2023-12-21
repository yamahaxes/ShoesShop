package ru.tracefamily.shoesshop.repository

import android.util.Base64
import dagger.hilt.android.scopes.ServiceScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.common.model.ConnectSettings
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocksRow
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks
import ru.tracefamily.shoesshop.domain.info.model.StocksCell
import ru.tracefamily.shoesshop.domain.info.model.StocksRow
import ru.tracefamily.shoesshop.domain.repo.InfoRepo
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.ApiInfoService
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.CardInfo
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.CommonStocksRowInfo
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.StocksCellInfo
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.StocksInfo
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.StocksRowInfo
import javax.inject.Inject


@ServiceScoped
class InfoRepoImpl @Inject constructor(override val connectSettings: ConnectSettings) : InfoRepo {

    override suspend fun getCard(barcode: Barcode): Result<Card> {
        val result = getInstance().create(ApiInfoService::class.java)
            .getCard(getCredentials(), barcode.value)

        val body = result.body()
        if (result.isSuccessful && body != null) {
            return Result.success(body.toCard())
        }
        return Result.failure(Throwable(result.message()))
    }

    override suspend fun getImage(barcode: Barcode): Result<Image> {
        val result = getInstance().create(ApiInfoService::class.java)
            .getBase64Image(getCredentials(), barcode.value)

        val body = result.body()
        if (result.isSuccessful && body != null) {
            return Result.success(body.toImage())
        }
        return Result.failure(Throwable(result.message()))
    }

    override suspend fun getStocks(barcode: Barcode): Result<Stocks> {
        val result = getInstance().create(ApiInfoService::class.java)
            .getStocks(getCredentials(), barcode.value)

        val body = result.body()
        if (result.isSuccessful && body != null) {
            return Result.success(body.toStocks())
        }
        return Result.failure(Throwable(result.message()))
    }

    override suspend fun getCommonStocks(barcode: Barcode): Result<List<CommonStocksRow>> {
        val result = getInstance().create(ApiInfoService::class.java)
            .getCommonStocks(getCredentials(), barcode.value)

        val body = result.body()
        if (result.isSuccessful && body != null) {
            return Result.success(body.map { it.toCommonStocksRow() })
        }
        return Result.failure(Throwable(result.message()))
    }

    private fun getInstance(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(connectSettings.serverAddress)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getCredentials(): String {
        val userPassword = String.format(
            "%s:%s",
            connectSettings.username,
            connectSettings.password
        )
        return "Basic " + Base64.encodeToString(userPassword.toByteArray(), Base64.NO_WRAP)
    }

}

fun CardInfo.toCard(): Card =
    Card(name = name, price = price, priceBeforeDiscount = priceBeforeDiscount)

fun String.toImage(): Image =
    Image(toString())

fun StocksInfo.toStocks(): Stocks =
    Stocks(
        rows.map { it.toStockRow() },
        cells.map { it.toStocksCell() }
    )

fun StocksRowInfo.toStockRow(): StocksRow = StocksRow(name = name, size = size, quantity = quantity)

fun StocksCellInfo.toStocksCell(): StocksCell = StocksCell(name = name, size = size, cell = cell)

fun CommonStocksRowInfo.toCommonStocksRow(): CommonStocksRow =
    CommonStocksRow(store = store, name = name, size = size, quantity = quantity)