package com.example.medstime.ui.common_components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medstime.R

/**### **AddMedButton** используется на фрагментах *AddMedTrackFragment*, *AddMedFragment*.
 * - В параметрах необходимо использовать либо контент, либо **iconId** и **stringId** */
@Composable
fun AddMedButton(
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit,
    @DrawableRes iconId: Int = 0,
    @StringRes stringId: Int = 0,
    content: @Composable RowScope.() -> Unit = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                text = stringResource(id = stringId),
                textAlign = TextAlign.Left
            )
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = iconId),
                contentDescription = null,
                alignment = Alignment.CenterEnd
            )
        }
    }
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.meds_time_primary),
            contentColor = colorResource(id = R.color.black)
        ),
        shape = RoundedCornerShape(16.dp),
        content = content
    )
}


@Preview(showBackground = true, backgroundColor = 0xFFEBEBEB)
@Composable
private fun PreviewAddMedButton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        AddMedButton(
            onClick = {},
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    text = stringResource(id = R.string.add_reminder),
                    textAlign = TextAlign.Left
                )
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.menu_icon_add),
                    contentDescription = null,
                    alignment = Alignment.CenterEnd
                )
            }
        }
        AddMedButton(
            onClick = {},
            iconId = R.drawable.menu_icon_meds_tracking,
            stringId = R.string.add_med_track
        )
    }
}