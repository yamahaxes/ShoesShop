package ru.tracefamily.shoesshop.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.tracefamily.shoesshop.domain.common.model.Barcode

interface BarcodeScannerRepo {
    fun startScanning(): Flow<Barcode?>
}