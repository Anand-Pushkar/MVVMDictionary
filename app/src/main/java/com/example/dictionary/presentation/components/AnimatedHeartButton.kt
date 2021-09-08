package com.example.dictionary.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dictionary.R

@SuppressLint("RememberReturnType")
@Composable
fun AnimatedHeartButton(
    isFavourite: Boolean,
    onToggle: () -> Unit

) {
    val idleIconSize = 24.dp
    val expandedIconSize = 32.dp

    val resource: Painter = if (isFavourite) {
        painterResource(id = R.drawable.ic_star_red)
    } else {
        painterResource(id = R.drawable.ic_star_black_border)
    }

    var sizeState by remember { mutableStateOf(expandedIconSize) }
    //var resourceState by remember { mutableStateOf(isFavourite) }

    val size by animateDpAsState(
        targetValue = sizeState,
        spring(Spring.DampingRatioMediumBouncy)
//        keyframes {
//            durationMillis = 500
//            expandedIconSize at 100
//            idleIconSize at 200
//        }
    )

    Image(
        modifier = Modifier
            .width(size)
            .height(size)
            .clickable(
                onClick = {
                    onToggle()
//                    if(isFavourite){
//                        sizeState = expandedIconSize
//                    }
//                    else{
//                        sizeState = idleIconSize
//                    }
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        painter = resource,
        contentDescription = "Heart"
    )


}

//@Composable
//private fun HeartButton(
//    isFavourite: Boolean,
//    modifier: Modifier,
//    size: Dp,
//    onToggle: () -> Unit,
//){
//    if (isFavourite){
//        loadPicture(drawable = R.drawable.heart_red).value.let { image ->
//            Image(
//                bitmap = image.asImageBitmap(),
//                modifier = Modifier
//                    .clickable(onClick = onToggle)
//                    .width(size)
//                    .height(size)
//            )
//        }
//    }
//    else{
//        loadPicture(drawable = R.drawable.heart_grey).value?.let { image ->
//            Image(
//                bitmap = image.asImageBitmap(),
//                modifier = Modifier
//                    .clickable(onClick = onToggle)
//                    .width(size)
//                    .height(size)
//            )
//        }
//    }
//}