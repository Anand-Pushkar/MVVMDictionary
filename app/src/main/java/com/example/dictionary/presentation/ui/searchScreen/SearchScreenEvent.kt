package com.example.dictionary.presentation.ui.searchScreen

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import java.time.format.TextStyle

sealed class SearchScreenEvent {

    data class OnQueryChangedEvent(
        val query: String
    ): SearchScreenEvent()

    data class OnTextFieldValueChanged(
        val tfv: TextFieldValue
    ): SearchScreenEvent()
}
