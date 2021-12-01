package com.example.dictionary.presentation.ui.rhymeDetails

import androidx.compose.material.ScaffoldState

sealed class RhymeDetailScreenEvent {

    data class GetRhymesEvent(
        val query: String
    ): RhymeDetailScreenEvent()

    data class AddToFavoritesEvent(
        val scaffoldState: ScaffoldState
    ): RhymeDetailScreenEvent()

    data class RemoveFromFavoritesEvent(
        val scaffoldState: ScaffoldState
    ): RhymeDetailScreenEvent()
}