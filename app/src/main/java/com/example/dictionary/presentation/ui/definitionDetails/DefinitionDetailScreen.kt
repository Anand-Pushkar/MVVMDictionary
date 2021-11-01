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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dictionary.R
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.presentation.components.*
import com.example.dictionary.presentation.components.util.SnackbarController
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.BlueTheme
import com.example.dictionary.presentation.theme.immersive_sys_ui
import com.example.dictionary.util.DEFINITION
import com.example.dictionary.util.LANDSCAPE
import com.example.dictionary.util.SAD_FACE
import com.example.dictionary.util.TAG
import java.util.*


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

    if (query.isEmpty()) {
        Log.d(TAG, "DefinitionDetailScreen: query is empty")
        // show invalid search or something like that
    } else {
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
                backgroundColor = MaterialTheme.colors.primary,
                snackbarHost = {
                    scaffoldState.snackbarHostState
                },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    BgCard(
                        modifier = Modifier
                            .fillMaxHeight(if(GetScreenOrientation() == LANDSCAPE){ 0.55f } else { 0.3f }),
                        isDark = isDark,
                        onNavigateToSearchScreen = onNavigateToSearchScreen,
                        def = definition,
                        loading = loading,
                        onLoad = onLoad,
                        addToFavorites = {
                            viewModel.onTriggerEvent(DefinitionDetailScreenEvent.AddToFavoritesEvent(scaffoldState))
                        },
                        removeFromFavorites = {
                            viewModel.onTriggerEvent(DefinitionDetailScreenEvent.RemoveFromFavoritesEvent(scaffoldState))
                        }
                    )
                    MainCard(
                        isDark = isDark,
                        def = definition,
                        loading = loading,
                        onLoad = onLoad,
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
    modifier: Modifier,
    isDark: MutableState<Boolean>,
    loading: Boolean,
    onLoad: Boolean,
    onNavigateToSearchScreen: (String) -> Unit,
    def: Definition?,
    addToFavorites: () -> Unit,
    removeFromFavorites: () -> Unit
) {
    Surface(
        color = if (isDark.value) {
            immersive_sys_ui
        } else {
            MaterialTheme.colors.surface
        },
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = if (GetScreenOrientation() == LANDSCAPE) {
                        36.dp
                    } else {
                        48.dp
                    }
                )
        ) {
            SearchAppBar(
                onNavigateToSearchScreen = onNavigateToSearchScreen,
                route = Screen.SEARCH_SCREEN_ROUTE.withArgs(DEFINITION)
            )

            if (loading && def == null) {
                LoadingListShimmer(
                    cardHeight = 35.dp,
                    cardWidth = 0.5f,
                    lineHeight = 24.dp,
                    lineWidth = 0.7f,
                    cardPadding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp),
                    lineSpace = if (GetScreenOrientation() == LANDSCAPE) { 16.dp } else { 48.dp }
                )
            } else if (!loading && def == null && onLoad) {
                // invalid search
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = "Invalid Search",
                    style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onPrimary),
                )
            } else def?.let { def ->

                if (def.defs != null) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(start = 20.dp),
                            text = def.word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                            style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onPrimary),
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 8.dp,
                                    start = 24.dp,
                                    end = if (GetScreenOrientation() == LANDSCAPE) { 48.dp } else { 24.dp }
                                )
                        ) {

                            Text(
                                text = "IPA : [ ${def.pronunciation} ]",
                                style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onPrimary),
                            )

                            val resource: Painter = if (def.isFavorite) {
                                painterResource(id = R.drawable.ic_star_red)
                            } else {
                                painterResource(R.drawable.ic_star_white_border)
                            }
                            Image(
                                modifier = Modifier
                                    .width(32.dp)
                                    .height(32.dp)
                                    .clickable(
                                        onClick = {
                                            if (!def.isFavorite) {
                                                addToFavorites()
                                            } else {
                                                removeFromFavorites()
                                            }
                                        },
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ),
                                painter = resource,
                                contentDescription = "Favorite"
                            )
                        }
                    }

                } else {
                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = def.word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                        style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onPrimary),
                    )
                }

            }
        }
    }
}

@Composable
fun MainCard(
    isDark: MutableState<Boolean>,
    loading: Boolean,
    onLoad: Boolean,
    def: Definition?,
) {
    Surface(
        color = if (isDark.value) {
            immersive_sys_ui
        } else {
            MaterialTheme.colors.surface
        },
        modifier = Modifier.fillMaxSize(),
    ){
        Surface(
            color = MaterialTheme.colors.primary,
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(40.dp)
                .copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize),
            elevation = 16.dp
        ) {
            if (loading && def == null) {
                LoadingListShimmer(
                    cardHeight = 30.dp,
                    cardWidth = 0.6f,
                    lineHeight = 24.dp,
                    lines = 3,
                    repetition = 3,
                    linePadding = PaddingValues(start = 32.dp, end = 8.dp)
                )

            } else if (!loading && def == null && onLoad) {
                NothingHere()
            } else def?.let { def ->

                if (def.defs != null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = 8.dp,
                                bottom = if (GetScreenOrientation() == LANDSCAPE) {
                                    8.dp
                                } else {
                                    48.dp
                                }
                            )
                    ) {
                        def.nouns?.let { nouns ->
                            if (nouns.isNotEmpty()) {
                                item {
                                    Section(type = "noun", list = nouns)
                                }
                            }
                        }
                        def.verbs?.let { verbs ->
                            if (verbs.isNotEmpty()) {
                                item {
                                    Section(type = "verb", list = verbs)
                                }
                            }
                        }
                        def.adjectives?.let { adjectives ->
                            if (adjectives.isNotEmpty()) {
                                item {
                                    Section(type = "adjective", list = adjectives)
                                }
                            }
                        }
                        def.adverbs?.let { adverbs ->
                            if (adverbs.isNotEmpty()) {
                                item {
                                    Section(type = "adverb", list = adverbs)
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
}


@Composable
fun Section(
    type: String,
    list: List<String>,
) {
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
            style = MaterialTheme.typography.h3.copy(
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colors.onPrimary
            )
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



