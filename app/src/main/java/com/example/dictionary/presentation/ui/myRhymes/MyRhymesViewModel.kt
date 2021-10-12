package com.example.dictionary.presentation.ui.myRhymes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.presentation.components.util.SnackbarController
import com.example.dictionary.presentation.ui.util.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MyRhymesViewModel
@Inject
constructor(

) : ViewModel() {

    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)
    val snackbarController = SnackbarController(viewModelScope)
}