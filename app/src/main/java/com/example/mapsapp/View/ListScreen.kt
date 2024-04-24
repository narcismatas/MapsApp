package com.example.mapsapp.View


import android.Manifest
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.Model.SavedMarker
import com.example.mapsapp.ViewModel.MainViewModel
import com.example.mapsapp.ViewModel.gilmer
import kotlinx.coroutines.launch

@Composable
fun ListScreen(navController: NavController, viewModel: MainViewModel, state: DrawerState) {
    ListScaffold(navController, viewModel, state)
}

@Composable
fun ListScaffold(navController: NavController, viewModel: MainViewModel, state: DrawerState) {
    Scaffold(
        topBar = { ListTopAppBar(viewModel, state) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFF6c757d))
        ) {
            val showDialog by viewModel.showDeleteDialog.observeAsState(false)
            if (showDialog) {
                DeleteAlert(viewModel = viewModel)
            }
            val colorFilter by viewModel.colorFilter.observeAsState("")
            val markers by viewModel.markers.observeAsState()

            if (colorFilter != "") {
                viewModel.getFilteredMarkers()
            } else {
                viewModel.getMarkers()
            }

            if (markers == null || markers!!.size == 0) {
                if (colorFilter == "") {
                    Text(
                        text = "No markers saved",
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Text(
                        text = "No ${colorFilter.lowercase()} markers saved",
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                        )
                    }
                    items(markers!!.toList()) { marker ->
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                        )
                        MarkerCard(marker = marker, viewModel, navController)
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                        )

                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                        )
                    }
                }
            }


        }

    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MarkerCard(marker: SavedMarker, viewModel: MainViewModel, navController: NavController) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .wrapContentHeight(),
        shape = RoundedCornerShape(20),
        onClick = {
            viewModel.getMarker(marker.markerId!!)
            navController.navigate(Routes.DetailScreen.route)
        },
        colors = CardColors(
            containerColor = Color.DarkGray,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (marker.image != null) {
                GlideImage(
                    model = marker.image,
                    contentDescription = "Marker Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20)),
                )
            } else {
                val context = LocalContext.current
                val isCameraPermissionGranted by viewModel.cameraPermissionGranted.observeAsState(false)
                val shouldShowPermissionRationale by viewModel.shouldShowPermissionRationale.observeAsState(
                    false
                )
                val showPermissionDenied by viewModel.showPermissionDenied.observeAsState(false)
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (isGranted) {
                            viewModel.setCameraPermissionGranted(true)
                            navController.navigate(Routes.CameraScreen.route)
                        } else {
                            viewModel.setShouldPermissionRationale(
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                    context as Activity,
                                    Manifest.permission.CAMERA
                                )
                            )
                            if (!shouldShowPermissionRationale) {
                                Log.i("CameraScreen", "No podemos volver a pedir permisos")
                                viewModel.setShowPermissionDenied(true)
                            }
                        }
                    }
                )
                val uri by viewModel.imageUri.observeAsState()
                FloatingActionButton(
                    onClick = {

                        viewModel.onTitleChange(marker.title)
                        viewModel.onDescChange(marker.description ?: "")
                        viewModel.onColorChange(marker.color)
                        if (!isCameraPermissionGranted) {
                            launcher.launch(Manifest.permission.CAMERA)
                        } else {
                            navController.navigate(Routes.CameraScreen.route)
                        }
                        if (showPermissionDenied) {
                            Toast.makeText(
                                context,
                                "Camera permission denied. Enable it through the settings",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        viewModel.clearImage()
                    },
                    modifier = Modifier
                        .height(100.dp)
                        .aspectRatio(1f),
                    containerColor = Color.Black,
                    shape = RoundedCornerShape(20)

                ) {
                    Icon(
                        imageVector = Icons.Filled.AddPhotoAlternate,
                        contentDescription = "Add image",
                        tint = Color.White,
                        modifier = Modifier.size(90.dp)
                    )
                }

                if (
                    uri != null &&
                    marker.title == viewModel.tempTitle.value &&
                    marker.description == viewModel.tempDesc.value &&
                    marker.color == viewModel.tempColor.value
                ) {
                    viewModel.addImage(uri!!, marker)
                    viewModel.hideMarkerSaving()
                    viewModel.clearImage()

                    if (viewModel.colorFilter.value != "") {
                        viewModel.getFilteredMarkers()
                    } else {
                        viewModel.getMarkers()
                    }

                }
            }

            Column(
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.Start)
                        .height(20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VerticalDivider(
                        modifier = Modifier.padding(end = 5.dp),
                        color = when (marker.color) {
                            "Red" -> Color(0xFFff0000)
                            "Green" -> Color(0xFF8cc63f)
                            "Yellow" -> Color(0xFFffff00)
                            "Blue" -> Color(0xFF0000ff)
                            "Cyan" -> Color(0xFF29abe2)
                            else -> Color(0xFF808080)
                        },
                        thickness = 8.dp
                    )
                    Text(
                        text = marker.title,
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(start = 15.dp, top = 5.dp)
                        .fillMaxWidth()
                        .height(5.dp),
                    color = Color.White,
                    thickness = 3.dp
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = if (marker.description != "") marker.description!! else "No description added",
                    style = TextStyle(
                        fontFamily = gilmer,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .align(Alignment.Start)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.width(15.dp))
            FloatingActionButton(
                onClick = {
                    viewModel.showDeleteDialog(marker.markerId!!)
                },
                modifier = Modifier
                    .height(50.dp)
                    .aspectRatio(1f)
                    .align(Alignment.Bottom),
                containerColor = Color.Black,
                shape = RoundedCornerShape(20)

            ) {
                Icon(
                    imageVector = Icons.Filled.DeleteForever,
                    contentDescription = "Add image",
                    tint = Color.Red,
                    modifier = Modifier.size(45.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopAppBar(viewModel: MainViewModel, state: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {
            Text(
                text = "Saved Markers",
                style = TextStyle(
                    fontFamily = gilmer,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    state.open()
                }
            }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
            }
        },
        actions = {
            val selectedFilter by viewModel.colorFilter.observeAsState("")
            var expanded by remember { mutableStateOf(false) }
            var colors = listOf("", "Blue", "Cyan", "Green", "Yellow", "Red", "LightGray")
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .animateContentSize()
                    .clip(shape = RoundedCornerShape(25))
            ) {
                FloatingActionButton(
                    onClick = { expanded = true },
                    contentColor = Color.Black,
                    containerColor = when (selectedFilter) {
                        "Red" -> Color(0xFFff0000)
                        "Green" -> Color(0xFF8cc63f)
                        "Yellow" -> Color(0xFFffff00)
                        "Blue" -> Color(0xFF0000ff)
                        "Cyan" -> Color(0xFF29abe2)
                        "LightGray" -> Color(0xFF808080)
                        else -> Color.White
                    },
                    shape = RoundedCornerShape(30),
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Colorize,
                        contentDescription = "Pick Color",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(0.dp)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.Black, RoundedCornerShape(25)),
                    properties = PopupProperties(focusable = true)
                ) {
                    colors.forEach { color ->
                        FloatingActionButton(
                            containerColor = when (color) {
                                "Red" -> Color(0xFFff0000)
                                "Green" -> Color(0xFF8cc63f)
                                "Yellow" -> Color(0xFFffff00)
                                "Blue" -> Color(0xFF0000ff)
                                "Cyan" -> Color(0xFF29abe2)
                                "LightGray" -> Color(0xFF808080)
                                else -> Color.Black
                            },
                            onClick = {
                                viewModel.changeColorFilter(color)
                                if (selectedFilter != "") {
                                    viewModel.getFilteredMarkers()
                                } else {
                                    viewModel.getMarkers()
                                }
                                expanded = false
                            },
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.5.dp)
                        ) {
                            if (color == "") {
                                Icon(
                                    imageVector = Icons.Filled.FormatColorReset,
                                    contentDescription = "Pick Color",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(0.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun DeleteAlert(viewModel: MainViewModel) {
    AlertDialog(
        icon = {
            Icon(
                Icons.Filled.Delete,
                "Delete",
                modifier = Modifier.size(50.dp),
                tint = Color.Red
            )
        },
        title = {
            Text(
                text = "Delete Marker?",
                style = TextStyle(
                    fontFamily = gilmer,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete the marker? This action is not reversible.",
                style = TextStyle(
                    fontFamily = gilmer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        },
        onDismissRequest = {
            viewModel.showDeleteDialog(null)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.deleteMarker()
                    viewModel.showDeleteDialog(null)
                }
            ) {
                Text(
                    "Confirm",
                    style = TextStyle(
                        fontFamily = gilmer,
                        color = Color.Red,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    viewModel.showDeleteDialog(null)
                }
            ) {
                Text(
                    "Dismiss",
                    style = TextStyle(
                        fontFamily = gilmer,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    )
}
