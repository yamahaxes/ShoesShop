package ru.tracefamily.shoesshop.domain.info.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.UseCaseExecutable
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.repo.InfoRepo
import javax.inject.Inject

@ViewModelScoped
class GetImageUseCase @Inject constructor(
    private val apiRepo: InfoRepo
) : UseCaseExecutable<Barcode, Image> {

    override suspend fun execute(input: Barcode): Result<Image> =
        try {
            Result.success(apiRepo.getImage(input))
        } catch (e: Throwable) {
            Result.failure(e)
        }
}