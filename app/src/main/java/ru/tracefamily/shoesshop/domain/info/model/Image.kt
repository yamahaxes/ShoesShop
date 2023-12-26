package ru.tracefamily.shoesshop.domain.info.model

data class Image(
    val base64Value: String
) {
    companion object {
        fun empty(): Image = Image("")
    }
}
