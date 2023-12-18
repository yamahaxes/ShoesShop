package ru.tracefamily.shoesshop.domain.info.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.CommonStocksRow
import ru.tracefamily.shoesshop.domain.repo.ApiInfoServiceRepo
import javax.inject.Inject

@ViewModelScoped
class GetCommonStocksUseCase @Inject constructor(
    private val apiRepo: ApiInfoServiceRepo
) {

    fun execute(barcode: Barcode): List<CommonStocksRow> {
        TODO()
    }

}