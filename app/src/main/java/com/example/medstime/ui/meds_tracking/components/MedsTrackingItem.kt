package com.example.medstime.ui.meds_tracking.components

import androidx.compose.animation.core.animateDp
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.MedsTrackModel
import com.example.medstime.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun MedsTrackingItem(trackModel: MedsTrackModel) {
    val expanded = remember { mutableStateOf(false) }//todo зависит от стейта?
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
            .clickable { expanded.value = !expanded.value },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MainPart(trackModel)
            ExpandablePart(trackModel, expanded)
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        ColumnWithIcon(
            drawableId = R.drawable.icon_calendar,
            text = getDisplayDate(trackModel.endDate),
        )
        ColumnWithIcon(
            drawableId = R.drawable.icon_box,
            text = trackModel.packageCounter.toString(),
        )
        ColumnWithIcon(
            drawableId = R.drawable.icon_cart,
            text = getDisplayDate(trackModel.recommendedPurchaseDate),
        )
    }
}

@Composable
fun ExpandablePart(trackModel: MedsTrackModel, expanded: MutableState<Boolean>) {
    val transition = updateTransition(expanded, label = "expandableLayoutTransition")
    val height by transition.animateDp(
        label = "expandableLayoutHeightTransition",
    ) { isExpanded ->
        if (isExpanded.value) 200.dp else 0.dp
    }
    val padding by transition.animateDp(
        label = "expandableLayoutPaddingTransition"
    ) { isExpanded ->
        if (isExpanded.value) 16.dp else 0.dp
    }
    Box(
        modifier = Modifier
            .height(height)
            .padding(vertical = padding)
            .fillMaxWidth()
            .background(Color.Gray),
        contentAlignment = Alignment.TopStart,
    ) {
        Text(
            text = "Дополнительное содержимое здесь...",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Composable
private fun ColumnWithIcon(drawableId: Int, text: String) {
    Column(
        modifier = Modifier.padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = drawableId),
            contentDescription = "",
        )
        Text(text = text, modifier = Modifier.padding(4.dp))
    }
}


private fun getDisplayDate(date: Long): String {
    val formatter = SimpleDateFormat("d MMMM y", Locale("ru", "RU"))
    val dateObject = Date(date)
    return formatter.format(dateObject)
}

@Preview(showBackground = true)
@Composable
private fun PreviewMedsTrackingItem() {
    val stub = listOf(
        MedsTrackModel(
            "111", "item 1",
            endDate = 1683844600900,
            packageCounter = 90,
            recommendedPurchaseDate = 1683963600900,
        ), MedsTrackModel(
            "111", "item 2",
            endDate = 1683844600900,
            packageCounter = 24,
            recommendedPurchaseDate = 1683963600900
        ),
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(stub) { _, item ->
            MedsTrackingItem(trackModel = item)
        }
    }
}