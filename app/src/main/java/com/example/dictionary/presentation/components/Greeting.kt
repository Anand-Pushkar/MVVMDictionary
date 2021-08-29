package com.example.dictionary.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dictionary.R

@Composable
fun GreetingSection(
    // pick up from datastore / shared prefs
    name: String = "Pushkar",
    isNetworkAvailable: MutableState<Boolean>,
    isDarkTheme: MutableState<Boolean>,
    onToggleTheme: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = if(isNetworkAvailable.value){ 48.dp } else { 0.dp },
                bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Good morning, $name",
                style = MaterialTheme.typography.h2,
            )
            Text(
                text = "We wish you have a good day!",
                style = MaterialTheme.typography.body1,
            )
        }
        // This icon can be used to change theme, or to navigate to settings screen, if we make one
        IconButton(
            onClick = { onToggleTheme() }
        ) {
            Icon(
                painter = if(isDarkTheme.value) painterResource(id = R.drawable.ic_light_theme) else painterResource(id = R.drawable.ic_dark_theme),
                contentDescription = "toggle theme",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}