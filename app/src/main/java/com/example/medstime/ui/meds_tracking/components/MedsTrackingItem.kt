package com.example.medstime.ui.meds_tracking.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.models.MedsTrackModel


@Composable
fun MedsTrackingItem(trackModel: MedsTrackModel) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column {
            Text(text = trackModel.name)
            ExpandableLayout()
        }
    }
}


@Composable
fun ExpandableLayout() {
    val expanded = remember { mutableStateOf(false) }
    val transition = updateTransition(expanded, label = "expandableLayoutTransition")

    val height by transition.animateDp(
        label = "expandableLayoutHeightTransition"
    ) { isExpanded ->
        if (isExpanded.value) 200.dp else 0.dp
    }

    val padding by transition.animateDp(
        label = "expandableLayoutPaddingTransition"
    ) { isExpanded ->
        if (isExpanded.value) 16.dp else 0.dp
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
    ) {
        Text(
            text = "Заголовок",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Дополнительный текст здесь...",
            maxLines = if (expanded.value) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { expanded.value = !expanded.value },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (expanded.value) "Скрыть" else "Показать")
        }
    }

    Box(
        modifier = Modifier
            .height(height)
            .fillMaxWidth()
            .background(Color.Gray),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = "Дополнительное содержимое здесь...",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMedsTrackingItem() {
    val stub = listOf(
        MedsTrackModel("111", "item 1"), MedsTrackModel("111", "item 2")
    )
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(stub) { _, item ->
            MedsTrackingItem(trackModel = item)
        }
    }
}
