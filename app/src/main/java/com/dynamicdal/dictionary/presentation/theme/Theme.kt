package com.dynamicdal.dictionary.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dynamicdal.dictionary.presentation.components.*
import com.dynamicdal.dictionary.presentation.ui.home.HomeTabs
import com.dynamicdal.dictionary.R
import java.util.*


private val YellowThemeLight = lightColors(
    primary = yellow200,
    onPrimary = Color.Black,
    primaryVariant = turquoiseVarient1,

    surface = yellow400,
    onSurface = Color.Black,

    secondary = blue200, // blue primary
    onSecondary = Color.White,
    secondaryVariant = blue400,

)

private val YellowThemeDark = darkColors( 
    primary = yellowDarkPrimary,
    onPrimary = Color.White,
    primaryVariant = turquoiseVarient2,

    secondary = yellow200, // primary will become secondary in dark theme
    onSecondary = Color.Black,

    onSurface = Color.White,
)

@ExperimentalMaterialApi
@Composable
fun YellowTheme(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    displayProgressBar: Boolean,
    scaffoldState: ScaffoldState,
    dialogQueue: Queue<GenericDialogInfo>,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme.value) {
        YellowThemeDark
    } else {
        YellowThemeLight
    }
    DictionaryTheme(
        darkTheme = darkTheme,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = displayProgressBar,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue,
        colors = colors,
        content = content
    )
}

private val BlueThemeLight = lightColors(
    primary = blue200,
    onPrimary = Color.White,
    primaryVariant = redVarient1,

    surface = blue400,
    onSurface = Color.White,

    secondary = yellow200, // yellow primary
    onSecondary = Color.Black,
    secondaryVariant = yellow400,
)

private val BlueThemeDark = darkColors(
    primary = blueDarkPrimary,
    onPrimary = Color.White,
    primaryVariant = redVarient2,

    secondary = blue200,
    onSecondary = Color.White,

    onSurface = Color.White,

)

@ExperimentalMaterialApi
@Composable
fun BlueTheme(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    scaffoldState: ScaffoldState,
    dialogQueue: Queue<GenericDialogInfo>,
    displayProgressBar: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme.value) {
        BlueThemeDark
    } else {
        BlueThemeLight
    }
    DictionaryTheme(
        darkTheme = darkTheme,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = displayProgressBar,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue,
        colors = colors,
        content = content
    )
}

@ExperimentalMaterialApi
@Composable
fun TabTheme(
    isDarkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    displayProgressBar: Boolean,
    scaffoldState: ScaffoldState,
    dialogQueue: Queue<GenericDialogInfo>,
    selectedTab: MutableState<String>,
    content: @Composable () -> Unit
){
    when (selectedTab.value) {
        HomeTabs.DEFINITION.id -> {
            BlueTheme(
                darkTheme = isDarkTheme,
                isNetworkAvailable = isNetworkAvailable,
                displayProgressBar = displayProgressBar,
                scaffoldState = scaffoldState,
                dialogQueue = dialogQueue,
                content = content
            )

        }
        HomeTabs.RHYME.id -> {
            YellowTheme(
                darkTheme = isDarkTheme,
                isNetworkAvailable = isNetworkAvailable,
                displayProgressBar = displayProgressBar,
                scaffoldState = scaffoldState,
                dialogQueue = dialogQueue,
                content = content
            )
        }
        else -> {
            BlueTheme(
                darkTheme = isDarkTheme,
                isNetworkAvailable = isNetworkAvailable,
                displayProgressBar = displayProgressBar,
                scaffoldState = scaffoldState,
                dialogQueue = dialogQueue,
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
    primary = pinkDarkPrimary,
    onPrimary = Color.White,

    secondary = pink500,
    surface = pink200
)

@ExperimentalMaterialApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun PinkTheme(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    displayProgressBar: Boolean,
    scaffoldState: ScaffoldState,
    dialogQueue: Queue<GenericDialogInfo>,
    content: @Composable () -> Unit
) {
    //val darkTheme: MutableState<Boolean> = mutableStateOf(isSystemInDarkTheme())
    val colors = if (darkTheme.value) {
        PinkThemeDark
    } else {
        PinkThemeLight
    }
    DictionaryTheme(
        darkTheme = darkTheme,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = displayProgressBar,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue,
        colors = colors,
        content = content
    )
}

private val LightElevation = Elevations(card = 8.dp)

private val DarkElevation = Elevations(card = 16.dp)

private val LightImages = Images(logo = R.mipmap.logo)

//private val DarkImages = Images(lockupLogo = R.drawable.ic_lockup_white)

@ExperimentalMaterialApi
@Composable
private fun DictionaryTheme(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    displayProgressBar: Boolean, // loading
    scaffoldState: ScaffoldState,
    dialogQueue: Queue<GenericDialogInfo>,
    colors: Colors,
    content: @Composable () -> Unit
) {
    val elevation = if (darkTheme.value) DarkElevation else LightElevation
    CompositionLocalProvider(
        LocalElevations provides elevation,
    ){
        MaterialTheme(
            colors = colors,
            typography = QuickSandTypography,
            shapes = Shapes,

        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.primary)
            ) {
                Column {
                    ConnectivityMonitor(
                        isNetworkAvailable = isNetworkAvailable,
                        darkTheme = darkTheme
                    )
                    content()
                }
                CircularIndeterminateProgressBar(
                    isDisplayed = displayProgressBar,
                    verticalBias = 0.3f
                )
                DefaultSnackbar(
                    darkTheme = darkTheme,
                    snackbarHostState = scaffoldState.snackbarHostState,
                    onDismiss = {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
                ProcessDialogQueue(dialogQueue = dialogQueue)
            }
        }
    }

}

@Composable
fun ProcessDialogQueue(
    dialogQueue: Queue<GenericDialogInfo>
){
    dialogQueue.peek()?.let { dialogInfo ->
        GenericDialog(
            onDismiss = dialogInfo.onDismiss,
            title = dialogInfo.title,
            description = dialogInfo.description,
            positiveAction = dialogInfo.positiveAction,
            negativeAction = dialogInfo.negativeAction
        )

    }
}



/**
 * Alternate to [MaterialTheme] allowing us to add our own theme systems (e.g. [Elevations]) or to
 * extend [MaterialTheme]'s types e.g. return our own [Colors] extension
 */
object DictionaryTheme {

    val colors: Colors
        @Composable
        get() = MaterialTheme.colors


    val typography: Typography
        @Composable
        get() = MaterialTheme.typography


    val shapes: Shapes
        @Composable
        get() = MaterialTheme.shapes


    val elevations: Elevations
        @Composable
        get() = LocalElevations.current


    val images: Images
        @Composable
        get() = LocalImages.current
}
