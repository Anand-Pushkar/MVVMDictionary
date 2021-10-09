package com.example.dictionary.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingListShimmer(
    cardHeight: Dp = 32.dp,
    cardWidth: Float = 1f,
    cardPadding: PaddingValues = PaddingValues(8.dp),
    lineHeight: Dp = 24.dp,
    padding: Dp = 16.dp,
    lines: Int = 1,
    repetition: Int = 1,
    linePadding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp),
    lineWidth: Float = 1f,
){

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        val cardWidthPx = with(LocalDensity.current) { (maxWidth - (padding*2)).toPx() }
        val cardHeightPx = with(LocalDensity.current) { (cardHeight - padding).toPx() }
        val gradientWidth: Float = (0.2f * cardHeightPx)

        val infiniteTransition = rememberInfiniteTransition()
        val xCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardWidthPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )
        val yCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardHeightPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val colors = listOf(
            Color.LightGray.copy(alpha = .9f),
            Color.LightGray.copy(alpha = .3f),
            Color.LightGray.copy(alpha = .9f),
        )

        if(repetition == 1){
            ShimmerCardItem(
                colors = colors,
                xShimmer = xCardShimmer.value,
                yShimmer = yCardShimmer.value,
                cardHeight = cardHeight,
                cardWidth = cardWidth,
                cardPadding = cardPadding,
                lineHeight = lineHeight,
                gradientWidth = gradientWidth,
                padding = padding,
                lines = lines,
                linePadding = linePadding,
                lineWidth = lineWidth
            )
        }else{
            LazyColumn {
                items(repetition){
                    ShimmerCardItem(
                        colors = colors,
                        xShimmer = xCardShimmer.value,
                        yShimmer = yCardShimmer.value,
                        cardHeight = cardHeight,
                        cardWidth = cardWidth,
                        cardPadding = cardPadding,
                        lineHeight = lineHeight,
                        gradientWidth = gradientWidth,
                        padding = padding,
                        lines = lines,
                        linePadding = linePadding,
                        lineWidth = lineWidth
                    )
                }
            }
        }

    }

}