package com.example.dictionary.presentation.ui.myRhymes

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionary.presentation.components.MyRhymesListItem
import com.example.dictionary.presentation.theme.YellowTheme
import com.example.dictionary.presentation.theme.immersive_sys_ui
import com.example.dictionary.util.TAG


val myRhymes = listOf("hello", "honor", "yo", "banana", "fat", "popular", "awesome", "yes")

@ExperimentalMaterialApi
@Composable
fun MyRhymesScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    viewModel: MyRhymesViewModel,
    onNavigateToDetailScreen: (String) -> Unit
) {

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
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    item {
                        TopRhymeBar()
                    }

                    itemsIndexed(myRhymes){ index, rhyme ->
                        MyRhyme(index = index)
                    }
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MyRhyme(
    //course: Course,
    index: Int,
    //selectCourse: (Long) -> Unit
) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        val stagger = if (index % 2 == 0) 72.dp else 16.dp
        Spacer(modifier = Modifier.width(stagger))
        MyRhymesListItem(
            modifier = Modifier.padding(top = 4.dp),
            onNavigateToDetailScreen = { Log.d(TAG, "MyRhyme: clicked") },
            shape = RoundedCornerShape(topStart = 24.dp),
        )
    }
}


@Composable
fun TopRhymeBar() {
    Row(
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            text = "My Rhymes",
            style = MaterialTheme.typography.h2.copy(
                fontSize = 32.sp,
                color = MaterialTheme.colors.primaryVariant
            ),
        )
    }
}