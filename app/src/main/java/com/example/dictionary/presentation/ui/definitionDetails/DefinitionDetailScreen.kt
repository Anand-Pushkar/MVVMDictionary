package com.example.dictionary.presentation.ui.definitionDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.dictionary.R
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.presentation.components.LoadingListShimmer
import com.example.dictionary.presentation.components.NothingHere
import com.example.dictionary.presentation.components.SearchAppBar
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.BlueTheme
import com.example.dictionary.util.DEFINITION
import com.example.dictionary.util.SAD_FACE
import com.example.dictionary.util.TAG
import java.util.*

val isFavourite = mutableStateOf(false)




@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun DefinitionDetailScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    viewModel: DefinitionDetailViewModel,
    onNavigateToSearchScreen: (String) -> Unit,
    query: String
) {

    if(query.isEmpty()){
        Log.d(TAG, "DefinitionDetailScreen: query is empty")
        // show invalid search or something like that
    }
    else{
        // fire a one-off event to get the definitions from api
        val onLoad = viewModel.onLoad.value
        if (!onLoad) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(DefinitionDetailScreenEvent.GetDefinitionDetailEvent(query))
        }

        val definition = viewModel.definition.value
        val loading = viewModel.loading.value
        val scaffoldState = rememberScaffoldState()
        val dialogQueue = viewModel.dialogQueue


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
                snackbarHost = {
                    scaffoldState.snackbarHostState
                },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BgCard(
                        isDark = isDark,
                        onNavigateToSearchScreen = onNavigateToSearchScreen,
                        def = definition,
                        loading = loading,
                        onLoad = onLoad,
                    )
                    MainCard(
                        def = definition,
                        loading = loading,
                        onLoad = onLoad
                    )
                }
            }
        }
    }
}


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun BgCard(
    isDark: MutableState<Boolean>,
    loading: Boolean,
    onLoad: Boolean,
    onNavigateToSearchScreen: (String) -> Unit,
    def: Definition?,
) {
    Surface(
        color = if(isDark.value){ Color.Black } else { MaterialTheme.colors.onPrimary },
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
                route = Screen.SEARCH_SCREEN_ROUTE.withArgs(DEFINITION)
            )

            if(loading && def == null){
                LoadingListShimmer(
                    cardHeight = 35.dp,
                    cardWidth = 0.5f,
                    lineHeight = 24.dp,
                    lineWidth = 0.7f,
                    cardPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 12.dp),
                    linePadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                )
            }
            else if (!loading && def == null && onLoad) {
                // invalid search
                Text(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                    text = "Invalid Search!",
                    style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onSecondary),
                )
            }
            else def?.let { def ->

                if(def.defs != null){

                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                        text = def.word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                        style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onSecondary),
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 24.dp)
                    ) {
                        val pron = def.tags[1].substringAfter(":")

                        Text(
                            text = "IPA : [ $pron ]",
                            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onSecondary),
                        )

                        val resource: Painter = if (isFavourite.value) {
                            painterResource(id = R.drawable.ic_star_red)
                        } else {
                            painterResource(id = if(isDark.value){ R.drawable.ic_star_white_border } else { R.drawable.ic_star_black_border })
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
                            contentDescription = "Favorite"
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                        text = def.word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                        style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onSecondary),
                    )
                }

            }
        }
    }
}

@Composable
fun MainCard(
    loading: Boolean,
    onLoad: Boolean,
    def: Definition?,
) {

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 260.dp),
        shape = RoundedCornerShape(40.dp)
            .copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize)
    ) {
        if(loading && def == null){
            LoadingListShimmer(
                cardHeight = 30.dp,
                cardWidth = 0.6f,
                lineHeight = 24.dp,
                lines = 3,
                repetition = 3,
                linePadding = PaddingValues(start = 32.dp, end = 8.dp)
            )

        }
        else if (!loading && def == null && onLoad) {
            NothingHere()
        }
        else def?.let { def ->

            if(def.defs != null){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp, top = 8.dp)
                ) {
                    def.nouns?.let { nouns ->
                        if(nouns.isNotEmpty()){
                            item {
                                Section(type = "noun", list = nouns)
                            }
                        }
                    }
                    def.verbs?.let { verbs ->
                        if(verbs.isNotEmpty()){
                            item {
                                Section(type = "verb", list = verbs)
                            }
                        }
                    }
                    def.adjectives?.let { adjectives ->
                        if(adjectives.isNotEmpty()){
                            item {
                                Section(type = "adjective", list = adjectives)
                            }
                        }
                    }
                }
            } else {
                NothingHere(
                    face = SAD_FACE,
                    text = "Definition not found!"
                )
            }
        }
    }
}


@Composable
fun Section(
    type: String,
    list: List<String>,
){
    Log.d(TAG, "Section: list size = ${list.size}")
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
            style = MaterialTheme.typography.h3.copy(fontStyle = FontStyle.Italic, color = MaterialTheme.colors.onPrimary)
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
                    style = MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.onPrimary)
                )
                Text(
                    text = "${listItem}.",
                    style = MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.onPrimary)
                )
            }
            index += 1
        }
    }
}



