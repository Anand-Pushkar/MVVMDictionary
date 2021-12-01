package com.example.dictionary.presentation.ui.onboarding

import androidx.compose.ui.text.input.TextFieldValue

sealed class OnboardingScreenEvent {

    data class OnTextFieldValueChanged(
        val tfv: TextFieldValue
    ): OnboardingScreenEvent()
}