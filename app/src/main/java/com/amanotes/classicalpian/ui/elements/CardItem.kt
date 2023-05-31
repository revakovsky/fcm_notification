package com.amanotes.classicalpian.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amanotes.classicalpian.R

@Composable
fun CardItem(
    modifier: Modifier,
    textContent: String
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(),
        elevation = CardDefaults.outlinedCardElevation(24.dp),
        modifier = modifier
            .padding(32.dp)
    ) {

        TitleText(textRes = R.string.your_max_score)

        Spacer(modifier = modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {

            Image(
                painter = painterResource(id = R.drawable.gold_coin),
                contentDescription = stringResource(id = R.string.gold_coin)
            )

            Spacer(modifier = modifier.width(16.dp))

            RegularText(text = textContent)

        }

        Spacer(modifier = modifier.height(32.dp))

    }

}