package com.dynamicdal.dictionary.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GenericTitleBar(
    modifier: Modifier = Modifier.padding(bottom = 16.dp),
    title: String,
    textColor: Color = MaterialTheme.colors.primaryVariant,
) {
    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            text = title,
            style = MaterialTheme.typography.h2.copy(
                fontSize = 32.sp,
                color = textColor
            ),
        )
    }
}