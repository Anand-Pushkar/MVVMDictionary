package com.dynamicdal.dictionary.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dynamicdal.dictionary.presentation.theme.RedErrorLight

@Composable
fun ConnectivityMonitor(
    isNetworkAvailable: MutableState<Boolean>,
    darkTheme: MutableState<Boolean>,
) {
    if(!isNetworkAvailable.value){
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 48.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                text = "No Network Connection!",
                style = MaterialTheme.typography.h6.copy(color = if(darkTheme.value){ RedErrorLight } else { MaterialTheme.colors.onPrimary })
            )
        }
    }
}
