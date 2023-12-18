package ru.tracefamily.shoesshop.repo

import dagger.hilt.android.scopes.ServiceScoped
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocksRow
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks
import ru.tracefamily.shoesshop.domain.repo.ApiInfoServiceRepo
import javax.inject.Inject

@ServiceScoped
class ApiInfoServiceRepoImpl @Inject constructor(): ApiInfoServiceRepo {

    override fun getCard(barcode: Barcode): Result<Card> {
        TODO("Not yet implemented")
    }

    override fun getImage(barcode: Barcode): Result<Image> {
        TODO("Not yet implemented")
    }

    override fun getStocks(barcode: Barcode): Result<Stocks> {
        TODO("Not yet implemented")
    }

    override fun getCommonStocks(barcode: Barcode): Result<List<CommonStocksRow>> {
        TODO("Not yet implemented")
    }

}