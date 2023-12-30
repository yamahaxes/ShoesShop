package ru.tracefamily.shoesshop.domain.info.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.UseCaseExecutable
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.repo.InfoRepo
import javax.inject.Inject

@ViewModelScoped
class GetCardUseCase @Inject constructor(
    private val apiRepo: InfoRepo
) : UseCaseExecutable<Barcode, Card> {

    override suspend fun execute(input: Barcode): Result<Card> =
        try {
            Result.success(apiRepo.getCard(input))
        } catch (e: Throwable) {
            Result.failure(e)
        }

}