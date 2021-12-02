package com.dynamicdal.dictionary.presentation.components.util

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
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
            scope.launch {
                scrollState.animateScrollTo(getScrollPosition())
                if (scrollState.value == getScrollPosition()) {
                    onChangeScrollPosition(scrollState.value)
                }
            }
        } else {
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
