package com.example.dictionary.presentation.theme

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.util.TAG
import okhttp3.internal.wait


private val YellowThemeLight = lightColors(
    primary = yellow200,
    onPrimary = Color.Black,

    surface = yellow400,
    onSurface = Color.Black,

    secondary = blue200, // blue primary
    onSecondary = Color.White,
    secondaryVariant = blue400,

)

private val YellowThemeDark = darkColors( 
    primary = yellowDarkPrimary,
    onPrimary = Color.White,

    secondary = yellow200, // primary will become secondary in dark theme
    onSecondary = Color.Black,

    onSurface = Color.White,
)

@Composable
fun YellowTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        YellowThemeDark
    } else {
        YellowThemeLight
    }
    DictionaryTheme(darkTheme, colors, content)
}

private val BlueThemeLight = lightColors(
    primary = blue200,
    onPrimary = Color.White,

    surface = blue400,
    onSurface = Color.White,

    secondary = yellow200, // yellow primary
    onSecondary = Color.Black,
    secondaryVariant = yellow400,
)

private val BlueThemeDark = darkColors(
    primary = blueDarkPrimary,
    onPrimary = Color.White,
    secondary = blue200,
    onSecondary = Color.White,

    onSurface = Color.White,

)

@Composable
fun BlueTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        BlueThemeDark
    } else {
        BlueThemeLight
    }
    DictionaryTheme(darkTheme, colors, content)
}

@Composable
fun TabTheme(
    isDarkTheme: Boolean,
    selectedTab: String,
    content: @Composable () -> Unit
){
    Log.d(TAG, "TabTheme: selectedTab = $selectedTab")
    when (selectedTab) {
        HomeTabs.DEFINITION.id -> {
            BlueTheme(
                darkTheme = isDarkTheme,
                content = content
            )

        }
        HomeTabs.RHYME.id -> {
            YellowTheme(
                darkTheme = isDarkTheme,
                content = content
            )
        }
        else -> {
            BlueTheme(
                darkTheme = isDarkTheme,
                content = content
            )
        }
    }
}

private val PinkThemeLight = lightColors(
    primary = pink500,
    secondary = pink500,
    primaryVariant = pink600,
    onPrimary = Color.Black,
    onSecondary = Color.Black
)

private val PinkThemeDark = darkColors(
    primary = pink200,
    secondary = pink200,
    surface = pinkDarkPrimary
)

@Composable
fun PinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        PinkThemeDark
    } else {
        PinkThemeLight
    }
    DictionaryTheme(darkTheme, colors, content)
}

private val LightElevation = Elevations(card = 4.dp)

private val DarkElevation = Elevations(card = 6.dp)

@Composable
private fun DictionaryTheme(
    darkTheme: Boolean,
    colors: Colors,
    content: @Composable () -> Unit
) {
    val elevation = if (darkTheme) DarkElevation else LightElevation
    CompositionLocalProvider(
        LocalElevations provides elevation,
    ){
        MaterialTheme(
            colors = colors,
            typography = QuickSandTypography,
            shapes = Shapes,
            content = content
        )
    }

}
