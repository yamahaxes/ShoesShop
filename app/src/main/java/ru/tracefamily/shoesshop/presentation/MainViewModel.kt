package ru.tracefamily.shoesshop.presentation

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.usecase.GetCardUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetCommonStocksUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetImageUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetStocksUseCase
import ru.tracefamily.shoesshop.domain.repo.BarcodeScannerRepo
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val barcodeBarcodeScannerRepo: BarcodeScannerRepo,
    private val getCardUseCase: GetCardUseCase,
    private val getImageUseCase: GetImageUseCase,
    private val getCommonStocksUseCase: GetCommonStocksUseCase,
    private val getStocksUseCase: GetStocksUseCase
): ViewModel() {

    private val _cardState = MutableStateFlow(Card(""))
    val cardState = _cardState.asStateFlow()

    private val _imageState = MutableStateFlow(Image(""))
    val imageState = _imageState.asStateFlow()

    fun scanBarcode() {
        viewModelScope.launch {
            barcodeBarcodeScannerRepo.startScanning().collect { barcode ->
                if (barcode != null) {
                    val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                    tone.startTone(ToneGenerator.TONE_SUP_PIP, 150)

                    updateCardInfo(barcode)
                    updateImage(barcode)

                }
            }
        }
    }

    private fun updateCardInfo(barcode: Barcode) {
        CoroutineScope(Dispatchers.IO).launch {
            _cardState.value = getCardUseCase.execute(barcode)
        }
    }

    private fun updateImage(barcode: Barcode) {
        CoroutineScope(Dispatchers.IO).launch {
            TODO()
        }
    }
}