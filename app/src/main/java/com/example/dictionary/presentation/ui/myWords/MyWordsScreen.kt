package com.example.dictionary.presentation.ui.myWords

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModel
import com.example.dictionary.R
import com.example.dictionary.domain.model.definition.DefinitionMinimal
import com.example.dictionary.presentation.components.*
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.BlueTheme
import com.example.dictionary.presentation.theme.immersive_sys_ui
import com.example.dictionary.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt


@SuppressLint("UnrememberedMutableState")
@ExperimentalMaterialApi
@Composable
fun MyWordsScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    viewModel: MyWordsViewModel,
    onNavigateToDetailScreen: (String) -> Unit
) {

    // use this so viewModel can observe lifecycle events of this composable
    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose { viewModel.onStop() }
    }

    val myWordsList = viewModel.myWordsList.value
    val scaffoldState = rememberScaffoldState()
    val dialogQueue = viewModel.dialogQueue
    val loading = viewModel.loading.value
    val comingBack = viewModel.comingBack


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
                    .padding(GetPadding())
            ) {
                val height = constraints.maxHeight
                val coroutineScope = rememberCoroutineScope()
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {

                    if(comingBack.value){
                        coroutineScope.launch {
                            scrollState.scrollTo(viewModel.getListScrollPosition())
                            if(scrollState.value == viewModel.getListScrollPosition()){
                                viewModel.comingBack.value = false
                            }
                        }
                    }else {
                        if (!scrollState.isScrollInProgress){
                            Log.d(TAG, "MyWordsScreen: here scroll not in progress")
                            coroutineScope.launch {
                                scrollState.scrollTo(viewModel.getListScrollPosition())
                                if(scrollState.value == viewModel.getListScrollPosition()){
                                    viewModel.onChangeScrollPosition(scrollState.value)
                                }
                            }

                        }else {
                            Log.d(TAG, "MyWordsScreen: scroll in progress")
                            viewModel.onChangeScrollPosition(scrollState.value)
                        }

                    }



                    GenericTitleBar(title = "My Words")
                    if (loading && myWordsList == null){
                        // shimmer
                        StaggeredVerticalGrid(
                            maxColumnWidth = 220.dp,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            repeat(12){
                                LoadingListShimmer(
                                    cardHeight = (200..260).random().dp,
                                    lines = 0,
                                    padding = 2.dp,
                                    cardPadding = PaddingValues(2.dp)
                                )
                            }
                        }
                    }
                    else if (!loading && myWordsList.isNullOrEmpty()) {
                        NothingHere(
                            modifier = Modifier.padding(top = (height / 8).dp),
                        )
                    }
                    else myWordsList?.let{ myWordsList ->
                        StaggeredVerticalGrid(
                            maxColumnWidth = 220.dp,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            
                            myWordsList.forEachIndexed { index, myWord ->
                                WordCard(
                                    myWord = myWord,
                                    onNavigateToDetailScreen = { route ->
                                        viewModel.onChangeScrollPosition(scrollState.value)
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
                text = myWord.word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
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


