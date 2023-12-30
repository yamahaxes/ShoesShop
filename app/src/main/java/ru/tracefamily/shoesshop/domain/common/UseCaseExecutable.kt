package ru.tracefamily.shoesshop.domain.common

interface UseCaseExecutable<R, T> {
    suspend fun execute(input: R): Result<T>
}