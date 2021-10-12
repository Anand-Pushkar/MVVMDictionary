package com.example.dictionary.presentation.ui.myWords

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.dictionary.R
import com.example.dictionary.domain.model.definition.DefinitionMinimal
import com.example.dictionary.presentation.components.LoadingListShimmer
import com.example.dictionary.presentation.components.NothingHere
import com.example.dictionary.presentation.components.OutlinedAvatar
import com.example.dictionary.presentation.components.StaggeredVerticalGrid
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.BlueTheme
import com.example.dictionary.presentation.theme.immersive_sys_ui
import com.example.dictionary.util.TAG


@ExperimentalMaterialApi
@Composable
fun MyWordsScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    viewModel: MyWordsViewModel,
    onNavigateToDetailScreen: (String) -> Unit
) {

    // fire a one-off event to get the definitions from cache

    val onLoad = viewModel.onLoad.value
    if (!onLoad) {
        viewModel.onLoad.value = true
        viewModel.onTriggerEvent(MyWordsScreenEvent.GetFavoriteWordsEvent)
    }

    val myWordsList = viewModel.myWordsList.value
    val scaffoldState = rememberScaffoldState()
    val dialogQueue = viewModel.dialogQueue
    val loading = viewModel.loading.value


    BlueTheme(
        darkTheme = isDark,
        isNetworkAvailable = isNetworkAvailable,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue.queue.value,
        displayProgressBar = loading,
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
            BoxWithConstraints(
                modifier = Modifier
                    .padding(top = 48.dp, bottom = 48.dp)
            ) {
                val height = constraints.maxHeight
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    TopWordBar()
                    if (loading && myWordsList == null){
                        // shimmer
                        Log.d(TAG, "MyWordsScreen: SHIMMER")
                        StaggeredVerticalGrid(
                            maxColumnWidth = 220.dp,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            repeat(8){
                                LoadingListShimmer(
                                    cardHeight = (200..260).random().dp,
                                    lines = 0,
                                    padding = 2.dp,
                                    cardPadding = PaddingValues(2.dp)
                                )
                            }
                        }
                    }
                    else if (!loading && myWordsList.isNullOrEmpty() && onLoad) {
                        NothingHere(
                            modifier = Modifier.padding(top = (height / 8).dp),
                        )
                    }
                    else myWordsList?.let{ myWordsList ->
                        StaggeredVerticalGrid(
                            maxColumnWidth = 220.dp,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            myWordsList.forEach { myWord ->
                                WordCard(
                                    myWord = myWord,
                                    onNavigateToDetailScreen = { route ->
                                        viewModel.onLoad.value = false
                                        onNavigateToDetailScreen(route)
                                    }
                                )
                            }

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

@ExperimentalMaterialApi
@Composable
fun WordCard(
    myWord: DefinitionMinimal,
    onNavigateToDetailScreen: (String) -> Unit
) {
    Card(
        onClick = {
            val route = Screen.DEFINITION_DETAIL_ROUTE.withArgs(myWord.word)
            onNavigateToDetailScreen(route)
        },
        modifier = Modifier
            .padding(4.dp),
        backgroundColor = MaterialTheme.colors.primary,
        elevation = 8.dp,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = myWord.word,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 4.dp)
                    .fillMaxWidth()
            )

            Text(
                text = "IPA : [ ${myWord.pronunciation} ]",
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
                text = myWord.statement,
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp)
                    .fillMaxWidth()
            )

        }
    }
}


