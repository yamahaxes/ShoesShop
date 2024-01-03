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
import ru.tracefamily.shoesshop.domain.common.model.Error
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.model.CommonStocks
import ru.tracefamily.shoesshop.domain.info.model.Image
import ru.tracefamily.shoesshop.domain.info.model.Stocks
import ru.tracefamily.shoesshop.domain.info.usecase.GetCardUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetCommonStocksUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetImageUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetStocksUseCase
import ru.tracefamily.shoesshop.domain.repo.BarcodeScannerRepo
import ru.tracefamily.shoesshop.domain.warehouse.model.DocType
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
    private val _errorMessageState = MutableStateFlow(ErrorState(mutableListOf()))
    val errorMessageState = _errorMessageState.asStateFlow()

    var currentScreen: String = ""

    fun scanBarcode() {
        viewModelScope.launch {
            barcodeBarcodeScannerRepo.startScanning().collect { barcode ->
                if (barcode != null) {
                    val tone = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                    tone.startTone(ToneGenerator.TONE_SUP_PIP, 150)

                    val errorState = ErrorState(mutableListOf())

                    if (currentScreen == Constants.ProductInfoNavItem.route) {
                        updateInfoState(barcode, errorState)
                    } else if (currentScreen == Constants.WarehouseManagementNavItem.route) {

                    }

                    if (errorState.errors.isNotEmpty()) {
                        _errorMessageState.value = errorState
                    }
                }
            }
        }
    }

    private suspend fun updateInfoState(barcode: Barcode, errorState: ErrorState) = coroutineScope {

        _loadingInfo.value = true

        val errors = errorState.errors

        // default values
        _infoState.value = InfoState()

        val cardDeferred = async {
            getCardUseCase.execute(barcode).getOrElse {
                errors.add(Error(R.string.message_error_load_card, it.toString()))
                Card(barcode = barcode)
            }
        }

        val imageDeferred = async {
            getImageUseCase.execute(barcode).getOrElse {
                errors.add(Error(R.string.message_error_load_image, it.toString()))
                Image()
            }
        }

        val stocksDeferred = async {
            getStocksUseCase.execute(barcode).getOrElse {
                errors.add(Error(R.string.message_error_load_stocks, it.toString()))
                Stocks()
            }
        }

        val commonStocksDeferred = async {
            getCommonStocksUseCase.execute(barcode).getOrElse {
                errors.add(Error(R.string.message_error_load_common_stocks, it.toString()))
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

    fun changeCurrentDocType(type: DocType) {
        viewModelScope.launch {
            updateWarehouseState(type)
        }
    }

    private suspend fun updateWarehouseState(docType: DocType) {
        val drafts = getDraftsUseCase.execute(docType).getOrDefault(listOf())
        _warehouseState.value = _warehouseState.value.copy(drafts = drafts, currentType = docType)
    }

    fun confirmErrors() {
        _errorMessageState.value = ErrorState(mutableListOf())
    }

}