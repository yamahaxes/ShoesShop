package ru.tracefamily.shoesshop.domain.info.usecase

import dagger.hilt.android.scopes.ViewModelScoped
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.repo.ApiInfoServiceRepo
import javax.inject.Inject

@ViewModelScoped
class GetImageUseCase @Inject constructor(
    private val apiRepo: ApiInfoServiceRepo
) {

    fun execute(barcode: Barcode): Image {
        TODO()
    }

}