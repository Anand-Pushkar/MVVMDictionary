package com.dynamicdal.dictionary.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import com.dynamicdal.dictionary.presentation.theme.blue100

@Composable
fun CircularIndeterminateProgressBar(
    isDisplayed: Boolean,
    verticalBias: Float
) {
    if (isDisplayed) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val progressBar = createRef()
            val topGuideline = createGuidelineFromTop(verticalBias)
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progressBar){
                    top.linkTo(topGuideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = blue100
            )
        }
    }
}
