package ru.tracefamily.shoesshop.presentation.screen

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import ru.tracefamily.shoesshop.R
import ru.tracefamily.shoesshop.presentation.MainViewModel
import ru.tracefamily.shoesshop.repository.utils.AutoSizeableText

@Composable
fun InfoScreen(vm: MainViewModel) {
    val stateScroll = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.verticalScroll(stateScroll)
        ) {
            ImageBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 15.dp)
                    .height(150.dp),
                vm
            )
            CardBlock(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 15.dp),
                vm
            )
            StocksBlock(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 15.dp),
                vm
            )
            CommonStocksBlock(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, top = 15.dp, bottom = 150.dp),
                vm
            )
        }
        LoadingInfo(
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp),
            vm
        )
    }
}

@Composable
private fun LoadingInfo(modifier: Modifier, vm: MainViewModel) {
    val stateLoading by vm.loadingInfo.collectAsState()
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Asset("loading.json"))

    if (stateLoading) {
        LottieAnimation(
            modifier = modifier,
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
    }
}

@Composable
private fun ImageBlock(modifier: Modifier, vm: MainViewModel) {
    val state = vm.imageState.collectAsState()

    Card(
        modifier = modifier
    ) {
        if (state.value.base64Value.isNotBlank()) {
            val decodedString: ByteArray =
                Base64.decode(state.value.base64Value, Base64.DEFAULT)
            val decodedByte =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            Image(
                bitmap = decodedByte.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            )
        }
    }
}

@Composable
private fun CardBlock(modifier: Modifier, vm: MainViewModel) {
    val state = vm.cardState.collectAsState()

    val discount = when {
        state.value.priceBeforeDiscount > state.value.price -> -(100 - state.value.price * 100 / state.value.priceBeforeDiscount) / 5 * 5
        else -> 0
    }

    Card(
        modifier = modifier
    ) {
        TextFieldInfo(
            label = stringResource(R.string.FieldLabelBarcode),
            text = state.value.barcode.value
        )
        TextFieldInfo(label = stringResource(R.string.FieldLabelName), text = state.value.name)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp),
            contentAlignment = Alignment.Center
        ) {
            if (discount == 0) {
                // Price without discount
                Text(
                    fontSize = 54.sp,
                    text = state.value.price.toString().plus(" ")
                        .plus(stringResource(R.string.RubleAbbreviation)),
                )
            } else {
                // Price with discount
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AutoSizeableText(
                        modifier = Modifier
                            .weight(0.2f),
                        style = TextStyle(textDecoration = TextDecoration.LineThrough),
                        maxTextSize = 22.sp,
                        minTextSize = 12.sp,
                        text = state.value.priceBeforeDiscount.toString()
                    )
                    AutoSizeableText(
                        text = state.value.price.toString().plus(" ")
                            .plus(stringResource(id = R.string.RubleAbbreviation)),
                        maxTextSize = 48.sp,
                        minTextSize = 10.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .weight(0.5f)
                    )
                    Box(
                        modifier = Modifier
                            .aspectRatio(1.0f)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.inversePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        AutoSizeableText(
                            modifier = Modifier
                                .padding(10.dp),
                            maxTextSize = 18.sp,
                            minTextSize = 6.sp,
                            text = discount.toString().plus("%")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StocksBlock(modifier: Modifier, vm: MainViewModel) {
    val state by vm.stocksState.collectAsState()

    val sizeColumnWeight = .3f
    val qualityColumnWeight = .3f
    val cellColumnWeight = .4f

    Box(modifier = modifier) {
        Column {

            // Heading
            Text(
                text = stringResource(R.string.LabelLeftoversInTheStore),
                modifier = Modifier.padding(bottom = 10.dp),
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            // Table header
            Row {
                TableCell(
                    text = stringResource(R.string.LabelColumnSize),
                    weight = sizeColumnWeight
                )
                TableCell(
                    text = stringResource(R.string.LabelColumnQuantity),
                    weight = qualityColumnWeight
                )
                TableCell(
                    text = stringResource(R.string.LabelColumnCell),
                    weight = cellColumnWeight
                )
            }
            Divider(thickness = 3.dp, modifier = Modifier.fillMaxWidth())
            // data
            val stocksList = state.rows
            val cellsList = state.cells

            if (stocksList.isNotEmpty()) {
                stocksList.sortedWith { o1, o2 ->
                    o1.size.compareTo(o2.size)
                }.forEach { item ->
                    Row {
                        TableCell(text = item.size, weight = sizeColumnWeight)
                        TableCell(
                            text = item.quantity.toString().plus(" ")
                                .plus(stringResource(R.string.PieceAbbreviation)),
                            weight = qualityColumnWeight
                        )
                        Column(Modifier.weight(cellColumnWeight)) {
                            cellsList.filter {
                                item.size.compareTo(it.size) == 0
                            }.sortedWith { o1, o2 ->
                                o1.cell.compareTo(o2.cell)
                            }.take(3).forEach {
                                Text(text = it.cell)
                            }
                        }
                    }
                    Divider(thickness = 1.dp, modifier = Modifier.fillMaxWidth())
                }
            } else {
                Text(text = stringResource(R.string.LabelMissing), fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
private fun CommonStocksBlock(modifier: Modifier, vm: MainViewModel) {

    val state by vm.commonStocksState.collectAsState()

    val inventory = state
    val stores = mutableSetOf<String>()
    inventory.forEach { stores.add(it.store) }

    Box(modifier = modifier) {
        Column {

            Text(
                text = stringResource(R.string.LabelShoppingLeftovers),
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )

            if (stores.isNotEmpty()) {
                stores.forEach { store ->

                    Divider(thickness = 3.dp, modifier = Modifier.fillMaxWidth())

                    Text(
                        text = store,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )

                    LazyRow(
                        modifier = Modifier.height(35.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        inventory.filter {
                            it.store == store
                        }.sortedWith { o1, o2 ->
                            o1.size.compareTo(o2.size)
                        }.forEach {
                            item {
                                Text(text = "${it.size} (${it.quantity})")
                                Spacer(modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            } else {
                Divider(thickness = 3.dp, modifier = Modifier.fillMaxWidth())
                Text(
                    text = stringResource(R.string.LabelMissing),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.fillMaxWidth()
                )
                Divider(thickness = 3.dp, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun TextFieldInfo(
    label: String,
    text: String,
    maxLines: Int = Int.MAX_VALUE
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            text = label,
        )
        Text(
            fontSize = 18.sp,
            modifier = Modifier
                .border(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp, start = 5.dp),
            text = text,
            maxLines = maxLines
        )
    }
}

@Composable
private fun RowScope.TableCell(text: String, weight: Float) {

    Text(
        text = text,
        Modifier
            .weight(weight)
            .padding(8.dp)
    )

}