package com.example.dictionary.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionary.util.SHRUG_FACE

@Composable
fun NothingHere(
    face: String = SHRUG_FACE,
    text: String = "There's nothing here",
){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .align(Alignment.Center)
        ){
            Text(
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                text = face,
                style = TextStyle(fontSize = 55.sp)
            )
            Text(
                modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally),
                text = text,
                style = MaterialTheme.typography.h4
            )
        }

    }
}