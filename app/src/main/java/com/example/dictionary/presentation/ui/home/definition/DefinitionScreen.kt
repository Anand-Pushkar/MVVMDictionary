package com.example.dictionary.presentation.ui.home.definition

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
fun DefinitionScreen(
    darkTheme: Boolean,
    modifier: Modifier,
    onToggleTheme: () -> Unit,
) {
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
            mainText = "My Words",
            secondaryText = "Favourite words show here",
        )
    }
}