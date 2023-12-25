package ru.tracefamily.shoesshop.domain.info.usecase

import ru.tracefamily.shoesshop.domain.common.model.Barcode

interface UseCaseExecutable<T> {
    suspend fun execute(barcode: Barcode): Result<T>
}