package com.example.dictionary.presentation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dictionary.presentation.theme.BlueTheme
import com.example.dictionary.presentation.components.GreetingSection
import com.example.dictionary.presentation.components.bottomNavigationComponent.DictionaryBottomBar
import com.example.dictionary.presentation.theme.TabTheme
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.util.TAG
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import java.util.*


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun DictionaryApp(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    ProvideWindowInsets {
        val tabs = remember { HomeTabs.values() }
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route?: HomeTabs.DEFINITION.route
        Log.d(TAG, "DictionaryApp: current route = ${navBackStackEntry?.destination?.route}")
        lateinit var currentTab: String
        tabs.forEach { tab ->
            if (currentRoute == tab.route){
                currentTab = tab.id
            }
        }
        Log.d(TAG, "DictionaryApp: currentTab = $currentTab")
        TabTheme(
            isDarkTheme = isDarkTheme,
            selectedTab = currentTab
        ) {

            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                backgroundColor = MaterialTheme.colors.primary,
                bottomBar = { DictionaryBottomBar(navController = navController, tabs = tabs) }
            ) {
                Column {
                    GreetingSection(
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { onToggleTheme() }
                    )
                    SearchAppBar()
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(it)
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    //query: String,
    //onQueryChanged: (String) -> Unit,
    //onExecuteSearch: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(32.dp)),
        color = MaterialTheme.colors.secondary,
        elevation = 8.dp,
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.5.dp),
            value = "hint",
            onValueChange = {
                //onQueryChanged(it)
            },
            label = {
                Text(text = "Search", color = MaterialTheme.colors.onPrimary)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon", tint = MaterialTheme.colors.onPrimary)
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    //onExecuteSearch()
                    keyboardController?.hide()
                }
            ),
            textStyle = TextStyle(color = MaterialTheme.colors.onPrimary),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small
                .copy(
                    topStart = CornerSize(32.dp),
                    topEnd = CornerSize(32.dp),
                    bottomEnd = CornerSize(32.dp),
                    bottomStart = CornerSize(32.dp)
                )
        )
    }
}



