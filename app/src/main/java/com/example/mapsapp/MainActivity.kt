package com.example.mapsapp

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.View.CameraScreen
import com.example.mapsapp.View.DetailScreen
import com.example.mapsapp.View.GalleryScreen
import com.example.mapsapp.View.HomeScreen
import com.example.mapsapp.View.ListScreen
import com.example.mapsapp.View.LogInScreen
import com.example.mapsapp.View.Map
import com.example.mapsapp.View.MyTopAppBar
import com.example.mapsapp.View.RegisterScreen
import com.example.mapsapp.View.Routes
import com.example.mapsapp.View.openAppSettings
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.ViewModel.MainViewModel
import com.example.mapsapp.ViewModel.gilmer
import com.example.mapsapp.dataStore.UserPrefs
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel by viewModels<MainViewModel>()
            MapsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.LogInScreen.route
                    ) {
                        composable(
                            Routes.HomeScreen.route,
                            enterTransition = {
                                fadeIn(
                                    animationSpec = tween(500, easing = EaseInOutExpo)
                                )
                            }
                        ) {
                            MyDrawer(mainViewModel, navController, "Home")
                            mainViewModel.hideMarkerSaving()
                        }
                        composable(Routes.ListScreen.route) {
                            MyDrawer(mainViewModel, navController, "List")
                        }
                        composable(Routes.CameraScreen.route) {
                            CameraScreen(navController, mainViewModel)
                        }
                        composable(Routes.GalleryScreen.route) {
                            GalleryScreen(navController, mainViewModel)
                        }
                        composable(
                            Routes.DetailScreen.route,
                            enterTransition = {
                                slideIntoContainer(
                                    animationSpec = tween(500, easing = EaseInOutExpo),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    animationSpec = tween(500, easing = EaseInOutExpo),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }
                        ) {
                            DetailScreen(navController, mainViewModel)
                        }
                        composable(
                            Routes.LogInScreen.route,
                            enterTransition = {
                                slideIntoContainer(
                                    animationSpec = tween(500, easing = EaseInOutExpo),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    animationSpec = tween(500, easing = EaseInOutExpo),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            }
                        ) {
                            LogInScreen(navController, mainViewModel)
                        }
                        composable(
                            Routes.RegisterScreen.route,
                            enterTransition = {
                                slideIntoContainer(
                                    animationSpec = tween(500, easing = EaseInOutExpo),
                                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                                )
                            },
                            exitTransition = {
                                slideOutOfContainer(
                                    animationSpec = tween(500, easing = EaseInOutExpo),
                                    towards = AnimatedContentTransitionScope.SlideDirection.End
                                )
                            }

                        ) {
                            RegisterScreen(navController, mainViewModel)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MyDrawer(viewModel: MainViewModel, navigationController: NavController, screen: String) {
    val scope = rememberCoroutineScope()
    val state: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerState = state,
        gesturesEnabled = state.isOpen,
        drawerContent = {
            ModalDrawerSheet(
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Menu",
                        modifier = Modifier.padding(0.dp),
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                        )
                    )
                    IconButton(onClick = {
                        scope.launch {
                            state.close()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Close",
                            Modifier.size(35.dp)
                        )
                    }
                }
                HorizontalDivider()
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Map",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    selected = screen == "Home",
                    onClick = {
                        val selectedScreen = "Home"
                        scope.launch {
                            state.close()
                        }
                        if (screen != selectedScreen) {
                            navigationController.navigate(Routes.HomeScreen.route)
                        }

                    },
                    shape = RoundedCornerShape(
                        topStartPercent = 0,
                        bottomStartPercent = 0,
                        topEndPercent = 100,
                        bottomEndPercent = 100
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Saved Markers",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    selected = screen == "List",
                    onClick = {
                        val selectedScreen = "List"
                        scope.launch {
                            state.close()
                        }
                        if (screen != selectedScreen) {
                            navigationController.navigate(Routes.ListScreen.route)
                        }
                    },
                    shape = RoundedCornerShape(
                        topStartPercent = 0,
                        bottomStartPercent = 0,
                        topEndPercent = 100,
                        bottomEndPercent = 100
                    ),
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
                Spacer(modifier = Modifier.weight(1f))
                val userPrefs = UserPrefs(LocalContext.current)
                val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.End)
                        .clickable {
                            scope.launch {
                                state.close()
                                if (viewModel.showLoading.value!!) {
                                    viewModel.modifyProcessing()
                                }
                                viewModel.logout()
                                userPrefs.saveUserData(
                                    storedUserData.value[0],
                                    storedUserData.value[1],
                                    "n"
                                )
                                navigationController.navigate(Routes.LogInScreen.route)
                            }
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Close",
                        Modifier.size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        "Log Out",
                        modifier = Modifier.padding(vertical = 16.dp),
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                        )
                    )

                }
            }
        }
    ) {

        when (screen) {
            "Home" -> HomeScreen(
                navController = navigationController,
                viewModel = viewModel,
                state = state
            )
            "List" -> ListScreen(navController = navigationController, viewModel = viewModel, state)
        }
    }
}
