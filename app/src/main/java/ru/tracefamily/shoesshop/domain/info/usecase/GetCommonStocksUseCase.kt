package ru.tracefamily.shoesshop.domain.info.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.UseCaseExecutable
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.CommonStocks
import ru.tracefamily.shoesshop.domain.repo.InfoRepo
import javax.inject.Inject

@ViewModelScoped
class GetCommonStocksUseCase @Inject constructor(
    private val apiRepo: InfoRepo
) : UseCaseExecutable<Barcode, CommonStocks> {

    override suspend fun execute(input: Barcode): Result<CommonStocks> =
        try {
            Result.success(apiRepo.getCommonStocks(input))
        } catch (e: Throwable) {
            Result.failure(e)
        }
}