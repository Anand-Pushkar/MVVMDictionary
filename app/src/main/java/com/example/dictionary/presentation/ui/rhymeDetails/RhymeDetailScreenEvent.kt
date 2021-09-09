package com.example.dictionary.presentation.ui.rhymeDetails

import com.example.dictionary.presentation.ui.definitionDetails.DefinitionDetailScreenEvent

sealed class RhymeDetailScreenEvent {

    data class GetRhymesEvent(
        val query: String
    ): RhymeDetailScreenEvent()
}