package com.example.dictionary.presentation.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionary.presentation.theme.PinkTheme
import com.example.dictionary.presentation.theme.pink500
import com.example.dictionary.presentation.theme.pinkDarkPrimary
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.TAG

@SuppressLint("UnrememberedMutableState")
@ExperimentalMaterialApi
@Composable
fun SplashScreen(){
    Log.d(TAG, "SplashScreen: here")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isSystemInDarkTheme()) {
                    pinkDarkPrimary
                } else {
                    pink500
                }
            )
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .align(Alignment.Center)
        ){
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "◕‿↼",
                style = TextStyle(fontSize = 55.sp)
            )
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "This is Splash Screen",
                style = MaterialTheme.typography.h4
            )
        }
    }

}