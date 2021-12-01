package com.example.dictionary.presentation.ui.onboarding

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.presentation.ui.searchScreen.SearchScreenEvent
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
@Inject
constructor(

) : ViewModel() {

    val query = mutableStateOf("")
    private val initialCursorPosition: MutableState<Int> = mutableStateOf(0)
    private val cursorPosition = initialCursorPosition.value.takeIf { it <= query.value.length } ?: query.value.length
    val textFieldValue = mutableStateOf(
        TextFieldValue(
            text = query.value,
            selection = TextRange(cursorPosition)
        )
    )
    val dialogQueue = DialogQueue()
    var isError = mutableStateOf(false)

    fun onTriggerEvent(event: OnboardingScreenEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is OnboardingScreenEvent.OnTextFieldValueChanged -> {
                        setTextFieldValue(event.tfv)
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    private fun setTextFieldValue(tfv: TextFieldValue) {
        this.textFieldValue.value = tfv
    }

    fun onSearchCleared() {
        setTextFieldValue(
            TextFieldValue(
                text = query.value,
                selection = TextRange(cursorPosition)
            )
        )
    }
}