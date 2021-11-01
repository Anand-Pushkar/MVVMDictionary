package com.example.dictionary.presentation.ui.myWords


sealed class MyWordsScreenEvent{

    object GetFavoriteWordsEvent: MyWordsScreenEvent()

    object RestoreStateEvent: MyWordsScreenEvent()

}

