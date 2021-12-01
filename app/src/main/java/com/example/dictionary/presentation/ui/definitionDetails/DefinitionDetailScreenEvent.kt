package com.example.dictionary.presentation.ui.definitionDetails

import androidx.compose.material.ScaffoldState

sealed class DefinitionDetailScreenEvent{

    data class GetDefinitionDetailEvent(
        val query: String
    ): DefinitionDetailScreenEvent()

    data class AddToFavoritesEvent(
        val scaffoldState: ScaffoldState
    ): DefinitionDetailScreenEvent()

    data class RemoveFromFavoritesEvent(
        val scaffoldState: ScaffoldState
    ): DefinitionDetailScreenEvent()
}