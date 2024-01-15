package ru.tracefamily.shoesshop.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.tracefamily.shoesshop.R
import ru.tracefamily.shoesshop.domain.warehouse.model.DocType
import ru.tracefamily.shoesshop.presentation.MainViewModel
import ru.tracefamily.shoesshop.presentation.model.FilterItem
import ru.tracefamily.shoesshop.presentation.utils.AutoSizeableText

@Composable
fun WarehouseScreen(modifier: Modifier, vm: MainViewModel) {

    Column(
        modifier = modifier
    ) {
        SegmentedButton(
            items = listOf(
                FilterItem(R.string.doctype_add, R.drawable.ic_add, DocType.ADD),
                FilterItem(R.string.doctype_withdraw, R.drawable.ic_minus, DocType.WITHDRAW),
                FilterItem(
                    R.string.doctype_recalculate,
                    R.drawable.ic_recalculation,
                    DocType.RECALCULATE
                )
            ),
            modifier = Modifier.padding(start = 5.dp, end = 5.dp, top = 5.dp),
            vm = vm
        )
        Divider()
        DocumentsList(Modifier.fillMaxSize(), vm)
    }
}

@Composable
fun DocumentsList(modifier: Modifier, vm: MainViewModel) {

    val state by vm.warehouseState.collectAsState()

    LazyColumn(
        modifier = modifier,
        content = {
            item {
                state.drafts.forEach {
                    ListItem(
                        modifier = Modifier
                            .clickable { vm.openDocument(it) }
                            .padding(horizontal = 5.dp, vertical = 3.dp),
                        headlineContent = { Text(text = it.cell) },
                        supportingContent = { Text(text = it.description) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Filled.Create,
                                contentDescription = null
                            )
                        },
                        shadowElevation = 3.dp,
                    )
                }


                Spacer(modifier = Modifier.height(150.dp))
            }
        })
}

@Composable
private fun SegmentedButton(
    items: List<FilterItem>,
    modifier: Modifier = Modifier,
    vm: MainViewModel
) {
    if (items.isEmpty()) {
        return
    }

    var enabledItem by rememberSaveable { mutableIntStateOf(0) }

    vm.changeCurrentDocType(items[enabledItem].type)

    Column(modifier = modifier) {
        Row(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)) {
            items.forEachIndexed { index, item ->
                OutlinedButton(
                    modifier = if (enabledItem == index) {
                        Modifier
                            .fillMaxHeight()
                            .weight(1.0f)
                    } else {
                        Modifier
                            .fillMaxHeight()
                            .weight(0.7f)
                    },
                    onClick = { enabledItem = index },
                    colors = if (enabledItem == index) ButtonDefaults.outlinedButtonColors(
                        MaterialTheme.colorScheme.primaryContainer
                    )
                    else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    shape = when (index) {
                        0 -> RoundedCornerShape(30, 0, 0, 0)
                        items.size - 1 -> RoundedCornerShape(0, 30, 0, 0)
                        else -> RoundedCornerShape(0, 0, 0, 0)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = stringResource(
                            id = item.textId
                        ),
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
        AutoSizeableText(
            text = stringResource(id = items[enabledItem].textId),
            maxTextSize = 26.sp,
            minTextSize = 10.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}