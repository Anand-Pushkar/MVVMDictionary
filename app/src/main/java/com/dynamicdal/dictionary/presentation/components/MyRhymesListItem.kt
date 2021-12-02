package com.dynamicdal.dictionary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.dynamicdal.dictionary.domain.model.rhyme.RhymesMinimal
import com.dynamicdal.dictionary.presentation.navigation.Screen
import java.util.*

@ExperimentalMaterialApi
@Composable
fun MyRhymesListItem(
    rhyme: RhymesMinimal,
    onNavigateToDetailScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    elevation: Dp = 8.dp,
) {
    Card(
        elevation = elevation,
        shape = shape,
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.primary,
        onClick = {
            val route = Screen.RHYME_DETAIL_ROUTE.withArgs(rhyme.word)
            onNavigateToDetailScreen(route)
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            val (horizontalLine, word, syllables, verticalLine) = createRefs()
            Spacer(
                // horizontal line
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = MaterialTheme.colors.secondary)
                    .constrainAs(horizontalLine) {
                        centerVerticallyTo(word)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 11.dp, bottom = 16.dp)
                    .constrainAs(word) {
                        top.linkTo(parent.top)
                        bottom.linkTo(syllables.top)
                        start.linkTo(parent.start)
                    }
                    .background(color = MaterialTheme.colors.primary),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                text = rhyme.word.replaceFirstChar {
                    if (it.isLowerCase())
                        it.titlecase(Locale.getDefault())
                    else
                        it.toString()
                }
            )
            Text(
                text = rhyme.syllableInfo,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .padding(start = 48.dp, end = 42.dp, bottom = 16.dp)
                    .background(color = MaterialTheme.colors.primary)
                    .constrainAs(syllables) {
                        start.linkTo(parent.start)
                        top.linkTo(word.bottom)
                        bottom.linkTo(parent.bottom)
                    }
            )
            Divider(
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 24.dp)
                    .width(2.dp)
                    .constrainAs(verticalLine) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    },
            )

        }
    }
}
