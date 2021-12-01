package com.example.dictionary.presentation.ui.myRhymes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dictionary.domain.model.rhyme.RhymesMinimal
import com.example.dictionary.presentation.components.*
import com.example.dictionary.presentation.components.util.manageScrollState
import com.example.dictionary.presentation.theme.YellowTheme
import com.example.dictionary.presentation.theme.immersive_sys_ui


@ExperimentalStdlibApi
@ExperimentalMaterialApi
@Composable
fun MyRhymesScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    viewModel: MyRhymesViewModel,
    onNavigateToDetailScreen: (String) -> Unit
) {

    // use this so viewModel can observe lifecycle events of this composable
    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose { viewModel.onStop() }
    }

    val myRhymesList = viewModel.myRhymesList.value
    val scaffoldState = rememberScaffoldState()
    val dialogQueue = viewModel.dialogQueue
    val loading = viewModel.loading.value
    val comingBack = viewModel.comingBack.value

    YellowTheme(
        darkTheme = isDark,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = loading,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue.queue.value
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
                    .padding(GetPadding())
            ){
                val height = constraints.maxHeight
                val coroutineScope = rememberCoroutineScope()
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {

                    manageScrollState(
                        comingBack = comingBack,
                        scope = coroutineScope,
                        scrollState = scrollState,
                        getScrollPosition = {
                            viewModel.getListScrollPosition()
                        },
                        onChangeScrollPosition = {
                            viewModel.onChangeScrollPosition(it)
                        },
                        setComingBackFalse = {
                            viewModel.comingBack.value = false
                        }
                    )

                    GenericTitleBar(title = "My Rhymes")

                    if (loading && myRhymesList == null) {
                        repeat(8){ index ->
                            Row(modifier = Modifier.padding(bottom = 12.dp)) {
                                val stagger = if (index % 2 == 0) 72.dp else 16.dp
                                Spacer(modifier = Modifier.width(stagger))
                                LoadingListShimmer(
                                    cardHeight = 100.dp,
                                    lines = 0,
                                    padding = 0.dp,
                                    shape = RoundedCornerShape(topStart = 24.dp),
                                    cardPadding = PaddingValues(0.dp)
                                )
                            }

                        }
                    } else if (!loading && myRhymesList.isNullOrEmpty()) {
                        NothingHere(
                            modifier = Modifier.padding(top = (height / 8).dp),
                        )
                    } else myRhymesList?.let { myRhymes ->
                        myRhymes.forEachIndexed { index, rhyme ->
                            MyRhyme(
                                rhyme = rhyme,
                                index = index,
                                onNavigateToDetailScreen = { route ->
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


@ExperimentalMaterialApi
@Composable
fun MyRhyme(
    rhyme: RhymesMinimal,
    index: Int,
    onNavigateToDetailScreen: (String) -> Unit
) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        val stagger = if (index % 2 == 0) 72.dp else 16.dp
        Spacer(modifier = Modifier.width(stagger))
        MyRhymesListItem(
            rhyme = rhyme,
            modifier = Modifier.padding(top = 4.dp),
            onNavigateToDetailScreen = onNavigateToDetailScreen,
            shape = RoundedCornerShape(topStart = 24.dp),
        )
    }
}

