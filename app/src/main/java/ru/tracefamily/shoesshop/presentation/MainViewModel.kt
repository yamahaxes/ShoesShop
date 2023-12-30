package ru.tracefamily.shoesshop.presentation

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tracefamily.shoesshop.R
import ru.tracefamily.shoesshop.domain.common.model.Barcode
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocks
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks
import ru.tracefamily.shoesshop.domain.info.usecase.GetCardUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetCommonStocksUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetImageUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetStocksUseCase
import ru.tracefamily.shoesshop.domain.repo.BarcodeScannerRepo
import ru.tracefamily.shoesshop.domain.warehouse.usecase.GetDraftsUseCase
import ru.tracefamily.shoesshop.domain.warehouse.usecase.PostDocumentUseCase
import ru.tracefamily.shoesshop.presentation.state.ErrorState
import ru.tracefamily.shoesshop.presentation.state.InfoState
import ru.tracefamily.shoesshop.presentation.state.WarehouseState
import ru.tracefamily.shoesshop.presentation.utils.Constants
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val barcodeBarcodeScannerRepo: BarcodeScannerRepo,
    private val getCardUseCase: GetCardUseCase,
    private val getImageUseCase: GetImageUseCase,
    private val getCommonStocksUseCase: GetCommonStocksUseCase,
    private val getStocksUseCase: GetStocksUseCase,
    private val getDraftsUseCase: GetDraftsUseCase,
    private val postDocumentUseCase: PostDocumentUseCase,
) : ViewModel() {

    // Info block state
    private val _infoState = MutableStateFlow(InfoState())
    val infoState = _infoState.asStateFlow()

    // Info block, state loading
    private val _loadingInfo = MutableStateFlow(false)
    val loadingInfo = _loadingInfo.asStateFlow()

    // Warehouse block state
    private val _warehouseState = MutableStateFlow(WarehouseState())
    val warehouseState = _warehouseState.asStateFlow()

    // If get error
    private val _errorMessageState = MutableStateFlow(ErrorState(-1, ""))
    val errorMessageState = _errorMessageState.asStateFlow()

    var currentScreen: String = ""

    fun scanBarcode() {
        viewModelScope.launch {
            barcodeBarcodeScannerRepo.startScanning().collect { barcode ->
                if (barcode != null) {
                    val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                    tone.startTone(ToneGenerator.TONE_SUP_PIP, 150)

                    if (currentScreen == Constants.ProductInfoNavItem.route) {
                        updateInfoState(barcode)
                    } else if (currentScreen == Constants.WarehouseManagementNavItem.route) {

                    }
                }
            }
        }
    }

    private suspend fun updateInfoState(barcode: Barcode) = coroutineScope {

        _loadingInfo.value = true

        // default values
        _infoState.value = InfoState()

        val cardDeferred = async {
            getCardUseCase.execute(barcode).getOrElse {
                _errorMessageState.value =
                    ErrorState(R.string.message_error_load_card, it.message ?: "")
                Card(barcode = barcode)
            }
        }

        val imageDeferred = async {
            getImageUseCase.execute(barcode).getOrElse {
                _errorMessageState.value =
                    ErrorState(R.string.message_error_load_image, it.message ?: "")
                Image()
            }
        }

        val stocksDeferred = async {
            getStocksUseCase.execute(barcode).getOrElse {
                _errorMessageState.value =
                    ErrorState(R.string.message_error_load_stocks, it.message ?: "")
                Stocks()
            }
        }

        val commonStocksDeferred = async {
            getCommonStocksUseCase.execute(barcode).getOrElse {
                _errorMessageState.value =
                    ErrorState(R.string.message_error_load_common_stocks, it.message ?: "")
                CommonStocks()
            }
        }

        _infoState.value = InfoState(
            cardDeferred.await(),
            imageDeferred.await(),
            stocksDeferred.await(),
            commonStocksDeferred.await()
        )

        _loadingInfo.value = false
    }

    fun confirmErrors() {
        _errorMessageState.value = ErrorState(-1, "")
    }

}