package com.example.dictionary.presentation.ui.searchScreen

import androidx.compose.runtime.MutableState

sealed class SearchScreenEvent {

    data class OnQueryChangedEvent(
        val query: String
    ): SearchScreenEvent()
    object UpdateSearchSuggestionsEvent: SearchScreenEvent()
}
