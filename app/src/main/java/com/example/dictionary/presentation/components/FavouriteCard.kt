package com.example.dictionary.presentation.components

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dictionary.R

@Composable
fun FavouriteCard(
    color: Color,
    mainText: String,
    secondaryText: String,
) {
    Row(
        //verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp, )
            //.height(100.dp) // not required, padding of the column make up for it
    ) {
        Column(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 36.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = mainText,
                style = MaterialTheme.typography.h2,
            )
            Text(
                text = secondaryText,
                style = MaterialTheme.typography.body2,
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .padding(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = "Favourite",
                tint = color,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
