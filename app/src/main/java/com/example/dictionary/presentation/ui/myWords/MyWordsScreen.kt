package com.example.dictionary.presentation.ui.myWords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.dictionary.R
import com.example.dictionary.presentation.components.OutlinedAvatar
import com.example.dictionary.presentation.theme.BlueTheme
import com.example.dictionary.presentation.theme.immersive_sys_ui
import com.example.dictionary.presentation.ui.util.DialogQueue
import kotlin.math.ceil


val myWords = listOf("yellow", "blue", "overwhelming", "absolute", "absolutely", "awesome", "honor", "honest")

val type = listOf("affected by jaundice which causes yellowing of skin etc", "the quality or state of the chromatic color resembling the hue of sunflowers or ripe lemons")

var counter: Boolean = true

@ExperimentalMaterialApi
@Composable
fun MyWordsScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    viewModel: MyWordsViewModel,
) {
    val scaffoldState = rememberScaffoldState()
    val dialogQueue = DialogQueue()

    BlueTheme(
        darkTheme = isDark,
        isNetworkAvailable = isNetworkAvailable,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue.queue.value,
        displayProgressBar = false, // change with loading
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            scaffoldState = scaffoldState,
            backgroundColor = immersive_sys_ui,
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 48.dp, bottom = 48.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    TopWordBar()
                    StaggeredVerticalGrid(
                        maxColumnWidth = 220.dp,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        myWords.forEach { myWord ->
                            FavoriteWord(myWord = myWord)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopWordBar() {
    Row(
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            text = "My Words",
            style = MaterialTheme.typography.h2.copy(
                fontSize = 32.sp,
                color = MaterialTheme.colors.primaryVariant
            ),
        )
    }
}

@Composable
fun FavoriteWord(
    myWord: String
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        color = MaterialTheme.colors.primary,
        elevation = 16.dp,
    ) {

        val type = if (counter) {
            type[0]
        } else {
            type[1]
        }
        counter = !counter

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = myWord,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 4.dp)
                    .fillMaxWidth()
            )

            Text(
                text = "[ jˈɛɫoʊ ]",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.subtitle2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            )

            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (line, avatar) = createRefs()
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = MaterialTheme.colors.secondary)
                        .constrainAs(line) {
                            centerVerticallyTo(parent)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                )
                OutlinedAvatar(
                    modifier = Modifier
                        .constrainAs(avatar) {
                            centerVerticallyTo(parent)
                            centerHorizontallyTo(parent)
                        },
                    res = R.drawable.ic_light_bulb_white,
                    size = 24.dp,
                    filledColor = MaterialTheme.colors.primaryVariant
                )
            }

            Text(
                text = type,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp )
                    .fillMaxWidth()
            )

        }
    }
}


@Composable
fun StaggeredVerticalGrid(
    modifier: Modifier = Modifier,
    maxColumnWidth: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        check(constraints.hasBoundedWidth) {
            "Unbounded width not supported"
        }
        val columns = ceil(constraints.maxWidth / maxColumnWidth.toPx()).toInt()
        val columnWidth = constraints.maxWidth / columns
        val itemConstraints = constraints.copy(maxWidth = columnWidth)
        val colHeights = IntArray(columns) { 0 } // track each column's height
        val placeables = measurables.map { measurable ->
            val column = shortestColumn(colHeights)
            val placeable = measurable.measure(itemConstraints)
            colHeights[column] += placeable.height
            placeable
        }

        val height = colHeights.maxOrNull()?.coerceIn(constraints.minHeight, constraints.maxHeight)
            ?: constraints.minHeight
        layout(
            width = constraints.maxWidth,
            height = height
        ) {
            val colY = IntArray(columns) { 0 }
            placeables.forEach { placeable ->
                val column = shortestColumn(colY)
                placeable.place(
                    x = columnWidth * column,
                    y = colY[column]
                )
                colY[column] += placeable.height
            }
        }
    }
}

private fun shortestColumn(colHeights: IntArray): Int {

    var minHeight = Int.MAX_VALUE
    var column = 0
    colHeights.forEachIndexed { index, height ->
        if (height < minHeight) {
            minHeight = height
            column = index
        }
    }
    return column
}
