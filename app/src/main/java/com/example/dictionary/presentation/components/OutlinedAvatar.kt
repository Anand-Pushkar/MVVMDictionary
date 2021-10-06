package com.example.dictionary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedAvatar(
    modifier: Modifier = Modifier,
    res: Int,
    size: Dp,
    outlineSize: Dp = 2.dp,
    outlineColor: Color = MaterialTheme.colors.secondary,
    filledColor: Color = MaterialTheme.colors.surface
) {
    Surface(
        modifier = modifier
            .clip(CircleShape),
        color = outlineColor,
        contentColor = Color.White
    ) {
        Box(
            modifier = modifier
                .padding(outlineSize)
                .background(
                    color = filledColor,
                    shape = CircleShape
                )
        ) {
            Column {
                Icon(
                    painter = painterResource(id = res),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(outlineSize)
                        .size(size)
                        .clip(CircleShape)
                )
            }
        }
    }
}
