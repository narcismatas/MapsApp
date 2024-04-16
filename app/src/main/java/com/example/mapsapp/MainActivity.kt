package com.example.mapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.View.CameraScreen
import com.example.mapsapp.View.GalleryScreen
import com.example.mapsapp.View.HomeScreen
import com.example.mapsapp.View.ListScreen
import com.example.mapsapp.View.LogInScreen
import com.example.mapsapp.View.Map
import com.example.mapsapp.View.MyTopAppBar
import com.example.mapsapp.View.RegisterScreen
import com.example.mapsapp.View.Routes
import com.example.mapsapp.ui.theme.MapsAppTheme
import com.example.mapsapp.ViewModel.MainViewModel
import com.example.mapsapp.ViewModel.gilmer
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
                        composable(Routes.HomeScreen.route) {
                            MyDrawer(mainViewModel, navController, "Home")
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
                        composable(Routes.LogInScreen.route) {
                            LogInScreen(navController, mainViewModel)
                        }
                        composable(Routes.RegisterScreen.route) {
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
                //modifier = Modifier.fillMaxWidth(0.6f)
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
                    shape = RoundedCornerShape(15)
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
                    shape = RoundedCornerShape(15)
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .align(Alignment.End)
                        .clickable {
                            scope.launch {
                                state.close()
                                viewModel.logout()
                                navigationController.navigate(Routes.LogInScreen.route)
                            }
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { TODO() }) {
                        Icon(
                            imageVector = Icons.Filled.Logout,
                            contentDescription = "Close",
                            Modifier.size(35.dp)
                        )
                    }
                    Text(
                        "Log Out",
                        modifier = Modifier.padding(0.dp),
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


