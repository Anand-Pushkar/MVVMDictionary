package com.example.dictionary.presentation.ui.definitionDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.dictionary.R
import com.example.dictionary.presentation.components.SearchAppBar
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.BlueTheme
import com.example.dictionary.presentation.theme.TabTheme
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.DEFINITION

val isFavourite = mutableStateOf(false)
val nounList = listOf(
    "the quality or state of the chromatic color resembling the hue of sunflowers or ripe lemons",
    "red", "Informal")

val verbList = listOf("turn yellow", "become yellow")

val adjectiveList = listOf(
    "cowardly or treacherous", "changed to a yellowish color by age", "similar to the color of an egg yolk",
    "affected by jaundice which causes yellowing of skin etc", "easily frightened", "cowardly or treacherous",
    "changed to a yellowish color by age", "similar to the color of an egg yolk",
    "affected by jaundice which causes yellowing of skin etc", "easily frightened")


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
//@Preview(showBackground = true)
@Composable
fun DefinitionDetailScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    onNavigateToSearchScreen: (String) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()

    BlueTheme(
        darkTheme = isDark,
        isNetworkAvailable = isNetworkAvailable,
        scaffoldState = scaffoldState,
        dialogQueue = DialogQueue().queue.value, // replace with the reference created in the viewModel
        displayProgressBar = false, // replace with loading
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                BgCard(
                    onNavigateToSearchScreen = onNavigateToSearchScreen
                )
                MainCard()
            }
        }
    }

}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun BgCard(
    onNavigateToSearchScreen: (String) -> Unit,
) {

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
            SearchAppBar(
                onNavigateToSearchScreen = onNavigateToSearchScreen,
                route = Screen.SEARCH_SCREEN_ROUTE.withArgs("definition")
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                text = "Yellow",
                style = MaterialTheme.typography.h1,
                color = Color.Black
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
                    style = MaterialTheme.typography.h5,
                    color = Color.Black
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp)
        ) {
            item {
                Section(type = "noun", list = nounList)
            }
            item {
                Section(type = "verb", list = verbList)
            }
            item {
                Section(type = "adjective", list = adjectiveList)
            }

        }
    }

}


@Composable
fun Section(
    type: String,
    list: List<String>,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = type,
            style = MaterialTheme.typography.h3.copy(fontStyle = FontStyle.Italic)
        )

        var index = 0
        list.forEach { listItem ->
            Row(
                modifier = Modifier
                    .padding(start = 18.dp, top = 6.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier
                        .padding(end = 12.dp),
                    text = "${index + 1}",
                    style = MaterialTheme.typography.h4
                )
                Text(
                    text = "${listItem}.",
                    style = MaterialTheme.typography.h4
                )
            }
            index += 1
        }
    }
}



