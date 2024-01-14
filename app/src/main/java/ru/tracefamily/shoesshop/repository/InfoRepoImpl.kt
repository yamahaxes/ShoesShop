package ru.tracefamily.shoesshop.repository

import android.content.Context
import android.util.Base64
import dagger.hilt.android.scopes.ServiceScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.exception.HttpException
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocks
import ru.tracefamily.shoesshop.domain.info.model.CommonStocksRow
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks
import ru.tracefamily.shoesshop.domain.info.model.StocksCell
import ru.tracefamily.shoesshop.domain.info.model.StocksRow
import ru.tracefamily.shoesshop.domain.repo.InfoRepo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.ApiInfoService
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.CardInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.CommonStocksInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.CommonStocksRowInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.ImageInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.StocksCellInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.StocksInfo
import ru.tracefamily.shoesshop.repository.apiservice.infoapi.model.StocksRowInfo
import ru.tracefamily.shoesshop.repository.model.ConnectSettings
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@ServiceScoped
class InfoRepoImpl @Inject constructor(
    private val connectSettings: ConnectSettings,
    private val context: Context
) : InfoRepo {

    @Throws(HttpException::class)
    override suspend fun getCard(barcode: Barcode): Card {
        val result = getInstance().create(ApiInfoService::class.java)
            .getCard(getCredentials(), barcode.value)

        return getResult(result, HttpURLConnection.HTTP_OK) {
            it?.toCard(barcode)
        } ?: Card()
    }

    @Throws(HttpException::class)
    override suspend fun getImage(barcode: Barcode): Image {
        val result = getInstance().create(ApiInfoService::class.java)
            .getBase64Image(getCredentials(), barcode.value)

        return getResult(result, HttpURLConnection.HTTP_OK) {
            it?.toImage()
        } ?: Image()

    }

    @Throws(HttpException::class)
    override suspend fun getStocks(barcode: Barcode): Stocks {
        val result = getInstance().create(ApiInfoService::class.java)
            .getStocks(getCredentials(), barcode.value)

        return getResult(result, HttpURLConnection.HTTP_OK) {
            it?.toStocks()
        } ?: Stocks()

    }

    @Throws(HttpException::class)
    override suspend fun getCommonStocks(barcode: Barcode): CommonStocks {

        val result = getInstance().create(ApiInfoService::class.java)
            .getCommonStocks(getCredentials(), barcode.value)

        return getResult(result, HttpURLConnection.HTTP_OK) {
            it?.toCommonStocks()
        } ?: CommonStocks()

    }

    @Throws(HttpException::class)
    private fun <T, R> getResult(
        result: Response<T>,
        successfulCode: Int,
        convert: (T?) -> (R?)
    ): R? {
        val body = result.body()
        if (result.isSuccessful) {
            return if (result.code() == successfulCode) {
                convert(body)
            } else {
                throw HttpException(result.message(), result.code())
            }
        }
        throw HttpException(result.message())
    }

    private fun getInstance(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client =
            OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(5, TimeUnit.SECONDS)
                .build()

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

fun CardInfo.toCard(barcode: Barcode = Barcode()): Card =
    Card(
        name = name,
        price = price,
        priceBeforeDiscount = priceBeforeDiscount,
        barcode = barcode
    )

fun ImageInfo.toImage(): Image =
    Image(base64Value = base64Value)

fun StocksInfo.toStocks(): Stocks =
    Stocks(
        stock.map { it.toStockRow() },
        cells.map { it.toStocksCell() }
    )

fun StocksRowInfo.toStockRow(): StocksRow = StocksRow(name = name, size = size, quantity = quantity)

fun StocksCellInfo.toStocksCell(): StocksCell = StocksCell(name = name, size = size, cell = cell)

fun CommonStocksInfo.toCommonStocks(): CommonStocks =
    CommonStocks(rows = stock.map { it.toCommonStocksRow() })

fun CommonStocksRowInfo.toCommonStocksRow(): CommonStocksRow =
    CommonStocksRow(store = store, name = name, size = size, quantity = quantity)