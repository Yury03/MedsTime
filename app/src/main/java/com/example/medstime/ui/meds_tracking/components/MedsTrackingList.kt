package com.example.medstime.ui.meds_tracking.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.domain.models.MedsTrackModel
import com.example.domain.models.PackageItemModel
import com.example.medstime.R
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MedsTrackingList(trackingList: List<MedsTrackModel>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(trackingList) { _, item ->
                MedsTrackingItem(trackModel = item)
            }
        }
        AddMedsTrackingItem()
    }
}

@Composable
fun AddMedsTrackingItem() {
    Card(
        modifier = Modifier
            .height(108.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(
                ContextCompat.getColor(
                    LocalContext.current,
                    R.color.screen_front,
                )
            ),
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.button_icon_plus),
                contentDescription = null
            )
        }

    }
}

@Composable
fun MedsTrackingItem(trackModel: MedsTrackModel) {
    val expanded = remember { mutableStateOf(false) }//todo зависит от стейта?
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .clickable { expanded.value = !expanded.value },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(
                ContextCompat.getColor(
                    LocalContext.current,
                    R.color.screen_front,
                )
            ),
        ),
    ) {
        val archive = SwipeAction(icon = (painterResource(id = R.drawable.button_icon_edit)),
            background = colorResource(id = R.color.selected_bottom_menu_item2),
            onSwipe = {/*переход на фрагмент редактирования*/ })

        SwipeableActionsBox(
            startActions = listOf(archive),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MainPart(trackModel)
                ExpandablePart(trackModel, expanded)
            }
        }
    }
}

@Composable
private fun MainPart(trackModel: MedsTrackModel) {
    Text(
        text = trackModel.name,
        fontSize = dimensionResource(id = R.dimen.text_4_level).value.sp,
        fontWeight = FontWeight.Bold,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        val beforeStr = stringResource(id = R.string.before)
        ColumnWithIcon(
            drawableId = R.drawable.icon_calendar,
            text = beforeStr.padEnd(beforeStr.length + 1) +
                    getDisplayDate(trackModel.endDate),
            modifier = Modifier//todo как вынести в переменную с weight?
                .padding(vertical = 8.dp)
                .weight(1f),
        )
        ColumnWithIcon(
            drawableId = R.drawable.icon_box,
            text = trackModel.packageCounter.toString(),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .background(colorResource(id = R.color.light_grey_background))
                .weight(1f),
        )
        ColumnWithIcon(
            drawableId = R.drawable.icon_cart,
            text = getDisplayDate(trackModel.recommendedPurchaseDate),
            modifier = Modifier //todo как вынести в переменную с weight?
                .padding(vertical = 8.dp)
                .weight(1f)
        )
    }
}

@Composable
fun ExpandablePart(
    trackModel: MedsTrackModel,
    expanded: MutableState<Boolean>,
) {
    val transition = updateTransition(expanded, label = "expandableLayoutTransition")
    val height by transition.animateDp(
        label = "expandableLayoutHeightTransition",
        transitionSpec = {
            tween(durationMillis = 600)
        }
    ) { isExpanded ->
        if (isExpanded.value) 110.dp else 0.dp//todo как исправить 150.dp на wrap_content
    }
    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .background(colorResource(id = R.color.screen_front)),
        contentAlignment = Alignment.TopStart,
    ) {
        LazyRow() {
            itemsIndexed(trackModel.packageItems) { _, item ->
                PackageItem(packageModel = item)
            }
        }
    }
}

@Composable
private fun ColumnWithIcon(drawableId: Int, text: String, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            modifier = Modifier.padding(vertical = 4.dp),
            painter = painterResource(id = drawableId),
            contentDescription = null,
        )
        Text(text = text, modifier = Modifier.padding(4.dp))
    }


}

private fun getDisplayDate(date: Long): String {
    val formatter = SimpleDateFormat("d MMMM y", Locale("ru", "RU"))
    val dateObject = Date(date)
    return formatter.format(dateObject)
}

@Composable
@Preview
private fun PreviewMedsTrackingList() {
    Box(modifier = Modifier.fillMaxSize()) {
        MedsTrackingList(trackingList = getTrackingListStub())
    }
}


fun getTrackingListStub(): List<MedsTrackModel> {
    val packageItemsStub = listOf(
        PackageItemModel(
            id = "fuisset",
            idMedsTrackModel = "verterem",
            intakesCount = 343,
            endDate = 1683963600900,
            expirationDate = 1683963600900
        ),
        PackageItemModel(
            id = "fuisset",
            idMedsTrackModel = "verterem",
            intakesCount = 33,
            endDate = 1683963600900,
            expirationDate = 1683963600900
        ),
        PackageItemModel(
            id = "fuisset",
            idMedsTrackModel = "verterem",
            intakesCount = 33,
            endDate = 1683963600900,
            expirationDate = 1683963600900
        ),
        PackageItemModel(
            id = "fuisset",
            idMedsTrackModel = "verterem",
            intakesCount = 33,
            endDate = 1683963600900,
            expirationDate = 1683963600900
        ),
    )
    return listOf(
        MedsTrackModel(
            "111", "item 1",
            endDate = 1683844600900,
            packageCounter = 90,
            recommendedPurchaseDate = 1683963600900,
            packageItems = packageItemsStub,
        ),
        MedsTrackModel(
            "111", "item 2",
            endDate = 1683844600900,
            packageCounter = 24,
            recommendedPurchaseDate = 1683963600900,
            packageItems = packageItemsStub,
        ),
    )
//    return emptyList()
}
