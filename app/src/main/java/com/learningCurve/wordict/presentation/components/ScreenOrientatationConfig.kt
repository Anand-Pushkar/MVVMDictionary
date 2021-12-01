package com.example.dictionary.presentation.components

import android.content.res.Configuration
import android.view.Display
import android.view.WindowManager
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.dictionary.util.LANDSCAPE

@Composable
fun GetPadding(): PaddingValues {
    if(GetScreenOrientation() == LANDSCAPE){
        return PaddingValues(top = 36.dp, bottom = 8.dp, end = 48.dp)
    }else {
        return PaddingValues(top = 48.dp, bottom = 48.dp)
    }
}

@Composable
fun GetScreenOrientation(): String {
    val configuration = LocalConfiguration.current
    configuration.screenLayout
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            return "Landscape"
        }
        else -> {
            return "Portrait"
        }
    }
}

