package ru.tracefamily.shoesshop.repository

import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.repo.BarcodeScannerRepo
import javax.inject.Inject

class BarcodeScannerRepoImpl @Inject constructor(private val scanner: GmsBarcodeScanner) :
    BarcodeScannerRepo {
    override fun startScanning(): Flow<Barcode?> {
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener {
                    launch { send(it.rawValue?.let { value -> Barcode(value) }) }
                }.addOnFailureListener { it.printStackTrace() }
            awaitClose()
        }
    }
}