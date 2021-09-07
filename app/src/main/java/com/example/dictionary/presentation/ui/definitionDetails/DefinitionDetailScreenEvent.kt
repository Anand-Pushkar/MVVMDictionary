package com.example.dictionary.presentation.ui.definitionDetails

sealed class DefinitionDetailScreenEvent{

    data class GetDefinitionDetailEvent(
        val query: String
    ): DefinitionDetailScreenEvent()
}