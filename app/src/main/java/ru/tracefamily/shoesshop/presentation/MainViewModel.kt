package ru.tracefamily.shoesshop.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.tracefamily.shoesshop.domain.info.model.Card
import ru.tracefamily.shoesshop.domain.info.usecase.GetCardUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetCommonStocksUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetImageUseCase
import ru.tracefamily.shoesshop.domain.info.usecase.GetStocksUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCardUseCase: GetCardUseCase,
    private val getImageUseCase: GetImageUseCase,
    private val getCommonStocksUseCase: GetCommonStocksUseCase,
    private val getStocksUseCase: GetStocksUseCase
): ViewModel() {
    private val _cardState = MutableStateFlow(Card(""))
    val cardState = _cardState.asStateFlow()


}