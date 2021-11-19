package com.example.dictionary.presentation.ui.onboarding

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionary.R
import com.example.dictionary.presentation.components.util.SnackbarController
import com.example.dictionary.presentation.theme.PinkTheme
import com.example.dictionary.presentation.theme.pinkDarkPrimary
import com.example.dictionary.util.TAG
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun OnboardingScreen(
    isNetworkAvailable: MutableState<Boolean>,
    viewModel: OnboardingViewModel,
    setUserName: (String) -> Unit,
    setOnboardingComplete: () -> Unit,
) {
    val dialogQueue = viewModel.dialogQueue
    val scaffoldState = rememberScaffoldState()
    val textFieldValue = viewModel.textFieldValue
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarController = SnackbarController(rememberCoroutineScope())
    val focusRequester = remember { FocusRequester() }
    val isError = viewModel.isError
    Log.d(TAG, "Onboarding: hello")

    PinkTheme(
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = false,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue.queue.value,
    ) {
        Scaffold(
            topBar = { AppBar() },
            backgroundColor = pinkDarkPrimary,
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        Log.d(TAG, "Onboarding: FAB onclick")
                        if (textFieldValue.value.text.isNotEmpty()) {
                            setUserName(textFieldValue.value.text)
                            setOnboardingComplete()
                        } else {
                            snackbarController.showSnackbar(
                                scaffoldState = scaffoldState,
                                message = "Name cannot be empty!",
                                actionLabel = "OK"
                            )
                            viewModel.isError.value = true
                            // can request focus here with : focusRequester.requestFocus()
                        }

                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Explore,
                        contentDescription = stringResource(R.string.label_continue)
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(innerPadding),
            ) {
                Text(
                    text = stringResource(R.string.label_enter_name),
                    style = MaterialTheme.typography.h1.copy(fontSize = 50.sp),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(
                        top = 32.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .focusRequester(focusRequester)
                        .onFocusEvent {
                            if(it.isFocused){
                                viewModel.isError.value = false
                            }
                        },
                    enabled = true,
                    isError = isError.value,
                    value = textFieldValue.value,
                    onValueChange = {
                        if (it.text.trim() == "") {
                            viewModel.onSearchCleared()
                        } else {
                            viewModel.onTriggerEvent(
                                OnboardingScreenEvent.OnTextFieldValueChanged(
                                    TextFieldValue().copy(
                                        text = it.text.trim(),
                                        selection = it.selection
                                    )
                                )
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h3.copy(fontSize = 30.sp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.primary,
                        focusedIndicatorColor = MaterialTheme.colors.secondary,
                        cursorColor = MaterialTheme.colors.onPrimary,
                        textColor = MaterialTheme.colors.onPrimary
                    ),
                )

                Spacer(Modifier.height(56.dp)) // center grid accounting for FAB
            }
        }
    }
}

@Composable
private fun AppBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.mipmap.logo_short),
            contentDescription = "logo short",
        )
    }
}


//@SuppressLint("UnrememberedMutableState")
//@ExperimentalMaterialApi
//@Preview(name = "Onboarding")
//@Composable
//private fun OnboardingPreview() {
//    Onboarding(
//        isNetworkAvailable = mutableStateOf(true)
//    ) {}
//}

