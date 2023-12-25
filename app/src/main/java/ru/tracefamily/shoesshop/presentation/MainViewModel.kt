package ru.tracefamily.shoesshop.presentation

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocksRow
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks
import ru.tracefamily.shoesshop.domain.info.usecase.GetCardUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetCommonStocksUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetImageUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetStocksUseCase
import ru.tracefamily.shoesshop.domain.repo.BarcodeScannerRepo
import ru.tracefamily.shoesshop.presentation.states.ErrorState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val barcodeBarcodeScannerRepo: BarcodeScannerRepo,
    private val getCardUseCase: GetCardUseCase,
    private val getImageUseCase: GetImageUseCase,
    private val getCommonStocksUseCase: GetCommonStocksUseCase,
    private val getStocksUseCase: GetStocksUseCase,
) : ViewModel() {

    private val tagLog: String = "MainViewModel"

    // Info block, card state
    private val _cardState = MutableStateFlow(Card(""))
    val cardState = _cardState.asStateFlow()

    // Info block, image state
    private val _imageState = MutableStateFlow(Image(""))
    val imageState = _imageState.asStateFlow()

    // Info block, stocks state
    private val _stocksState = MutableStateFlow(Stocks(listOf(), listOf()))
    val stocksState = _stocksState.asStateFlow()

    // Info block, common stocks state
    private val _commonStocksState = MutableStateFlow<List<CommonStocksRow>>(listOf())
    val commonStocksState = _commonStocksState.asStateFlow()

    // If get error
    private val _errorMessageState = MutableStateFlow(ErrorState())
    val errorMessageState = _errorMessageState.asStateFlow()

    fun scanBarcode() {
        viewModelScope.launch {
            barcodeBarcodeScannerRepo.startScanning().collect { barcode ->
                if (barcode != null) {
                    val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                    tone.startTone(ToneGenerator.TONE_SUP_PIP, 150)

                    updateInfoState(barcode)
                }
            }
        }
    }

    private suspend fun updateInfoState(barcode: Barcode) {

        // default values
        _cardState.value = Card("")
        _imageState.value = Image("")
        _stocksState.value = Stocks(listOf(), listOf())
        _commonStocksState.value = listOf()

        try {
            _cardState.value = getCardUseCase.execute(barcode).getOrThrow()
            _imageState.value = getImageUseCase.execute(barcode).getOrThrow()
            //_stocksState.value = getStocksUseCase.execute(barcode).getOrThrow()
            //_commonStocksState.value = getCommonStocksUseCase.execute(barcode).getOrThrow()
        } catch (e: Throwable) {
            _errorMessageState.value = ErrorState(e.message.toString())
            Log.d("MyLog", e.message.toString())
        }
    }

    fun confirmErrors() {
        _errorMessageState.value = ErrorState("")
    }

}