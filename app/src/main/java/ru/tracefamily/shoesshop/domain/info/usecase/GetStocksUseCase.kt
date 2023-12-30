package ru.tracefamily.shoesshop.domain.info.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.UseCaseExecutable
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Stocks
import ru.tracefamily.shoesshop.domain.repo.InfoRepo
import javax.inject.Inject

@ViewModelScoped
class GetStocksUseCase @Inject constructor(
    private val apiInfo: InfoRepo
) : UseCaseExecutable<Barcode, Stocks> {
    override suspend fun execute(input: Barcode): Result<Stocks> =
        try {
            Result.success(apiInfo.getStocks(input))
        } catch (e: Throwable) {
            Result.failure(e)
        }
}