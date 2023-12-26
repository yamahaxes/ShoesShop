package ru.tracefamily.shoesshop.domain.common.model

data class Barcode(
    val value: String
) {
    companion object {
        fun empty(): Barcode = Barcode("")
    }
}
