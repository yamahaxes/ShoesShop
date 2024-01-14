package ru.tracefamily.shoesshop.domain.common

import ru.tracefamily.shoesshop.domain.common.model.Barcode

fun isBarcodeOfCell(barcode: Barcode): Boolean =
    barcode.value.length == 5 && barcode.value[1] == '-' && barcode.value[3] == '-'