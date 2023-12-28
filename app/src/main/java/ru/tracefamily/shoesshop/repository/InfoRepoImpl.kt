package ru.tracefamily.shoesshop.repository

import android.content.Context
import android.util.Base64
import dagger.hilt.android.scopes.ServiceScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tracefamily.shoesshop.R
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
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.ImageInfo
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.StocksCellInfo
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.StocksInfo
import ru.tracefamily.shoesshop.repository.httpservice.infoapi.model.StocksRowInfo
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@ServiceScoped
class InfoRepoImpl @Inject constructor(
    override val connectSettings: ConnectSettings,
    private val context: Context
) : InfoRepo {

    override suspend fun getCard(barcode: Barcode): Result<Card> {
        val result = getInstance().create(ApiInfoService::class.java)
            .getCard(getCredentials(), barcode.value)

        val body = result.body()
        if (result.isSuccessful) {
            return if (result.code() == HttpURLConnection.HTTP_OK) {
                if (body != null) {
                    Result.success(body.toCard().copy(barcode = barcode))
                } else {
                    Result.success(Card().copy(barcode = barcode))
                }
            } else if (result.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                Result.failure(Throwable(context.getString(R.string.MessageTheCardNotFoundByBarcode)))
            } else {
                Result.failure(Throwable(context.getString(R.string.MessageErrorReceiptCardWithCodeError).plus(result.code())))
            }
        }
        return Result.failure(Throwable(result.message()))
    }

    override suspend fun getImage(barcode: Barcode): Result<Image> {
        val result = getInstance().create(ApiInfoService::class.java)
            .getBase64Image(getCredentials(), barcode.value)

        val body = result.body()
        if (result.isSuccessful) {
            return if (body != null) {
                Result.success(body.toImage())
            } else {
                Result.success(Image())
            }

        }
        return Result.failure(Throwable(result.message()))
    }

    override suspend fun getStocks(barcode: Barcode): Result<Stocks> {
        val result = getInstance().create(ApiInfoService::class.java)
            .getStocks(getCredentials(), barcode.value)

        val body = result.body()
        if (result.isSuccessful) {
            return if (body != null) {
                Result.success(body.toStocks())
            } else {
                Result.success(Stocks())
            }
        }
        return Result.failure(Throwable(result.message()))
    }

    override suspend fun getCommonStocks(barcode: Barcode): Result<List<CommonStocksRow>> {
        val result = getInstance().create(ApiInfoService::class.java)
            .getCommonStocks(getCredentials(), barcode.value)

        val body = result.body()
        if (result.isSuccessful) {
            return if (body != null) {
                Result.success(body.stock.map { it.toCommonStocksRow() })
            } else {
                Result.success(listOf())
            }
        }
        return Result.failure(Throwable(result.message()))
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

fun CardInfo.toCard(): Card =
    Card(
        name = name,
        price = price,
        priceBeforeDiscount = priceBeforeDiscount,
        barcode = Barcode()
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

fun CommonStocksRowInfo.toCommonStocksRow(): CommonStocksRow =
    CommonStocksRow(store = store, name = name, size = size, quantity = quantity)