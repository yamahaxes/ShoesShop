package ru.tracefamily.shoesshop.domain.repo

import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocksRow
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks

interface InfoRepo {

    suspend fun getCard(barcode: Barcode): Card

    suspend fun getImage(barcode: Barcode): Image

    suspend fun getStocks(barcode: Barcode): Stocks

    suspend fun getCommonStocks(barcode: Barcode): List<CommonStocksRow>

}