package com.dynamicdal.dictionary.presentation.ui.searchScreen

import androidx.compose.ui.text.input.TextFieldValue

sealed class SearchScreenEvent {

    data class OnTextFieldValueChanged(
        val tfv: TextFieldValue
    ): SearchScreenEvent()

    object OnSearchCleared: SearchScreenEvent()
}
