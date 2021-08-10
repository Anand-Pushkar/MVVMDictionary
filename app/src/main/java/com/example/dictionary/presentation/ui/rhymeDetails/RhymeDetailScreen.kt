package com.example.dictionary.presentation.ui.rhymeDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.dictionary.R
import com.example.dictionary.presentation.components.SearchAppBar

val isFavourite = mutableStateOf(false)

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun RhymeDetailScreen(){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        BgCard()
        MainCard()
    }
}

@ExperimentalComposeUiApi
@Composable
fun BgCard() {

    Surface(
        color = MaterialTheme.colors.onPrimary,
        modifier = Modifier.fillMaxSize(),
        elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
        ) {
            SearchAppBar()
            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                text = "Yellow",
                style = MaterialTheme.typography.h1
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = "pronunciation",
                    style = MaterialTheme.typography.h5
                )

                val resource: Painter = if (isFavourite.value) {
                    painterResource(id = R.drawable.ic_star_red)
                } else {
                    painterResource(id = R.drawable.ic_star_border)
                }
                Image(
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp)
                        .clickable(
                            onClick = {
                                isFavourite.value = !isFavourite.value
                            },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ),
                    painter = resource,
                    contentDescription = "Heart"
                )

            }
        }
    }
}

@Composable
fun MainCard() {

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 260.dp),
        shape = RoundedCornerShape(40.dp)
            .copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize)
    ) {

    }
}