package com.example.dictionary.presentation.ui.myRhymes

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionary.domain.model.rhyme.RhymesMinimal
import com.example.dictionary.presentation.components.GenericTitleBar
import com.example.dictionary.presentation.components.LoadingListShimmer
import com.example.dictionary.presentation.components.MyRhymesListItem
import com.example.dictionary.presentation.components.NothingHere
import com.example.dictionary.presentation.theme.YellowTheme
import com.example.dictionary.presentation.theme.immersive_sys_ui
import com.example.dictionary.presentation.ui.myWords.MyWordsScreenEvent
import com.example.dictionary.util.TAG


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

    // fire a one-off event to get the rhymes from cache
    val onLoad = viewModel.onLoad.value
    if (!onLoad) {
        viewModel.onLoad.value = true
        viewModel.onTriggerEvent(MyRhymesScreenEvent.GetFavoriteRhymesEvent)
    }

    val myRhymesList = viewModel.myRhymesList.value
    val scaffoldState = rememberScaffoldState()
    val dialogQueue = viewModel.dialogQueue
    val loading = viewModel.loading.value

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
            Column(
                modifier = Modifier
                    .padding(top = 48.dp, bottom = 48.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                } else if (!loading && myRhymesList.isNullOrEmpty() && onLoad) {
                    NothingHere()
                } else myRhymesList?.let { myRhymes ->

                    myRhymes.forEachIndexed { index, rhyme ->
                        MyRhyme(
                            rhyme = rhyme,
                            index = index,
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

