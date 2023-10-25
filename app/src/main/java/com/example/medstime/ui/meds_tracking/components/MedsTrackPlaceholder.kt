package com.example.medstime.ui.meds_tracking.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.medstime.R

@Composable
fun MedsTrackPlaceholder() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.placeholder_capybara),
            contentDescription = null,
            modifier = Modifier.wrapContentWidth(),
        )
        Text(
            text = stringResource(id = R.string.placeholder_meds_track_fragment),
            fontSize = dimensionResource(id = R.dimen.text_4_level).value.sp,
            color = colorResource(id = R.color.secondary_text),
            textAlign = TextAlign.Center,
            modifier = Modifier.wrapContentWidth(),
            //todo работает по-другому в отличие от xml с теми же параметрами
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMedsTrackPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MedsTrackPlaceholder()
    }
}