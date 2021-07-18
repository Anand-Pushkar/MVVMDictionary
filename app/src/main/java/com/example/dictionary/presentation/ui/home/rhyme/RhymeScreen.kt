package com.example.dictionary.presentation.ui.home.rhyme

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dictionary.presentation.components.FavouriteCard
import com.example.dictionary.presentation.components.GreetingSection
import com.example.dictionary.presentation.components.SearchAppBar

@ExperimentalComposeUiApi
@Composable
fun RhymeScreen(
    darkTheme: Boolean,
    modifier: Modifier,
    onToggleTheme: () -> Unit,
){
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        GreetingSection(
            isDarkTheme = darkTheme,
            onToggleTheme = { onToggleTheme() }
        )
        SearchAppBar()
        FavouriteCard(
            color = MaterialTheme.colors.primaryVariant,
            mainText = "My Rhymes",
            secondaryText = "Favourite rhymes show here",
        )
    }
}