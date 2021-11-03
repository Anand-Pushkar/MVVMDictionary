package com.example.dictionary.presentation.components.util

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import com.example.dictionary.util.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
fun manageScrollState(
    comingBack: Boolean,
    scope: CoroutineScope,
    scrollState: ScrollState,
    getScrollPosition: () -> Int,
    onChangeScrollPosition: (Int) -> Unit,
    setComingBackFalse: () -> Unit,
) {
    if (comingBack) {
        scope.launch {
            scrollState.scrollTo(getScrollPosition())
            if (scrollState.value == getScrollPosition()) {
                setComingBackFalse()
            }
        }
    } else {
        if (!scrollState.isScrollInProgress) {
            Log.d(TAG, "manageScrollState: not scrolling")
            scope.launch {
                Log.d(TAG, "manageScrollState: getScrollPosition = ${getScrollPosition()}")
                scrollState.animateScrollTo(getScrollPosition())
                if (scrollState.value == getScrollPosition()) {
                    Log.d(TAG, "manageScrollState: scrollstate.value = ${scrollState.value}")
                    onChangeScrollPosition(scrollState.value)
                }
            }
        } else {
            Log.d(TAG, "manageScrollState: scrolling = ${scrollState.value}")
            onChangeScrollPosition(scrollState.value)
        }
    }
}

fun manageScrollStateForLazyColumn(
    scope: CoroutineScope,
    scrollState: LazyListState,
    getScrollIndex: () -> Int,
    getScrollOffset: () -> Int,
    updateScrollState: (Int, Int) -> Unit
) {
    if(!scrollState.isScrollInProgress){
        scope.launch {
            scrollState.animateScrollToItem(getScrollIndex(), getScrollOffset())
            if(scrollState.firstVisibleItemIndex == getScrollIndex()
                && scrollState.firstVisibleItemScrollOffset == getScrollOffset()){
                    updateScrollState(scrollState.firstVisibleItemIndex, scrollState.firstVisibleItemScrollOffset)
            }
        }
    } else {
        updateScrollState(scrollState.firstVisibleItemIndex, scrollState.firstVisibleItemScrollOffset)
    }
}
