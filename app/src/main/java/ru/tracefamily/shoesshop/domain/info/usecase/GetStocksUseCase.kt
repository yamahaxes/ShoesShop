package ru.tracefamily.shoesshop.domain.info.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Stocks
import ru.tracefamily.shoesshop.domain.repo.ApiInfoServiceRepo
import javax.inject.Inject

@ViewModelScoped
class GetStocksUseCase @Inject constructor(
    private val apiInfo: ApiInfoServiceRepo
) {

    fun execute(barcode: Barcode): Stocks {
        TODO()
    }

}