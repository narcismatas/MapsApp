package com.example.mapsapp.View

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.mapsapp.Model.SavedMarker
import com.example.mapsapp.R
import com.example.mapsapp.ViewModel.MainViewModel
import com.example.mapsapp.ViewModel.gilmer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

@Composable
fun DetailScreen(navController: NavController, viewModel: MainViewModel) {
    DetailScaffold(navController, viewModel)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScaffold(navController: NavController, viewModel: MainViewModel) {
    val marker by viewModel.currentMarker.observeAsState()
    Scaffold(
        topBar = { DetailTopAppBar(navController, viewModel) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFF6c757d))
        ) {
            FloatingActionButton(
                onClick = {
                    viewModel.showEditMarker(
                        marker!!.title,
                        marker!!.description!!,
                        marker!!.color
                    )
                },
                modifier = Modifier
                    .zIndex(5f)
                    .align(Alignment.BottomEnd)
                    .offset(x = (-20).dp, y = (-20).dp),
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                Icon(imageVector = Icons.Filled.ModeEdit, contentDescription = "Edit")
            }
            EditMarker(navController = navController, viewModel = viewModel)
            val listState = rememberLazyListState()
            val showButton by remember {
                derivedStateOf {
                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index != listState.layoutInfo.totalItemsCount - 1
                }
            }
            val coroutineScope = rememberCoroutineScope()
            AnimatedVisibility(
                visible = showButton,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
                modifier = Modifier
                    .zIndex(10f)
                    .align(Alignment.TopEnd)
                    .offset(x = (-20).dp, y = 20.dp)

            ) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollBy(
                                value = 1500f,
                                animationSpec = tween(durationMillis = 1000, easing = EaseInOut)
                            )

                        }
                    },
                    containerColor = Color.White,
                    contentColor = Color.Black
                ) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "Scroll to Location"
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                state = listState
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(25.dp),
                        colors = CardColors(
                            containerColor = Color.Black,
                            contentColor = Color.White,
                            disabledContainerColor = Color.White,
                            disabledContentColor = Color.Black
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp)
                                    .wrapContentHeight(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = marker!!.title,
                                    style = TextStyle(
                                        fontFamily = gilmer,
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Black
                                    ),
                                )

                            }


                            HorizontalDivider(
                                Modifier.fillMaxWidth(),
                                thickness = 4.dp,
                                color = when (marker!!.color) {
                                    "Red" -> Color(0xFFff0000)
                                    "Green" -> Color(0xFF8cc63f)
                                    "Yellow" -> Color(0xFFffff00)
                                    "Blue" -> Color(0xFF0000ff)
                                    "Cyan" -> Color(0xFF29abe2)
                                    else -> Color(0xFF808080)
                                }
                            )

                            if (marker!!.description!! != "") {
                                var size by remember { mutableStateOf(marker!!.description!!.length) }
                                if (size > 200) {
                                    var expanded by remember { mutableStateOf(false) }
                                    Text(
                                        text = if (expanded) marker!!.description!! else marker!!.description!!.substring(
                                            0,
                                            150
                                        ) + "...",
                                        style = TextStyle(
                                            fontFamily = gilmer,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Light
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 10.dp, bottom = 5.dp)
                                            .clickable {
                                                expanded = !expanded
                                            }
                                            .animateContentSize()
                                    )
                                } else {
                                    Text(
                                        text = marker!!.description!!,
                                        style = TextStyle(
                                            fontFamily = gilmer,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Light
                                        ),
                                        modifier = Modifier
                                            .align(Alignment.Start)
                                            .padding(top = 10.dp, bottom = 5.dp)
                                    )
                                }
                            }
                        }

                    }
                }
                if (marker!!.image != null) {
                    item { Spacer(modifier = Modifier.height(10.dp)) }
                    item {
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .wrapContentHeight(),
                            shape = RoundedCornerShape(30.dp),
                            colors = CardColors(
                                containerColor = Color.Black,
                                contentColor = Color.White,
                                disabledContainerColor = Color.White,
                                disabledContentColor = Color.Black
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(15.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Image",
                                    style = TextStyle(
                                        fontFamily = gilmer,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(bottom = 5.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth() // Ocupa el ancho máximo
                                        .clip(RoundedCornerShape(25.dp))
                                ) {
                                    GlideImage(
                                        model = marker!!.image,
                                        contentDescription = "Marker Image",
                                        contentScale = ContentScale.FillWidth,
                                    )
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .wrapContentHeight(),
                        shape = RoundedCornerShape(30.dp),
                        colors = CardColors(
                            containerColor = Color.Black,
                            contentColor = Color.White,
                            disabledContainerColor = Color.White,
                            disabledContentColor = Color.Black
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Location",
                                style = TextStyle(
                                    fontFamily = gilmer,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(bottom = 5.dp)
                            )
                            DetailMap(marker!!)
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(10.dp)) }
            }
        }
    }
}

@Composable
fun DetailMap(marker: SavedMarker) {
    val position = LatLng(marker.latitude, marker.longitude)
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1 / 1.5f)
            .clip(RoundedCornerShape(25.dp)),
        cameraPositionState = CameraPositionState(CameraPosition(position, 12f, 0f, 0f)),
        uiSettings = MapUiSettings(
            compassEnabled = false,
            tiltGesturesEnabled = false,
            myLocationButtonEnabled = false,
            zoomControlsEnabled = false,
            zoomGesturesEnabled = false,
            scrollGesturesEnabled = false
        ),
        properties = MapProperties(
            isBuildingEnabled = true,
            mapType = MapType.NORMAL,
            isMyLocationEnabled = false,
        )
    ) {
        val icon = when (marker.color) {
            "Red" -> R.drawable.red_marker
            "Green" -> R.drawable.green_marker
            "Yellow" -> R.drawable.yellow_marker
            "Cyan" -> R.drawable.cyan_marker
            "Blue" -> R.drawable.blue_marker
            else -> R.drawable.gray_marker
        }
        Marker(
            state = MarkerState(LatLng(marker.latitude, marker.longitude)),
            draggable = false,
            icon = BitmapDescriptorFactory.fromResource(icon)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(navController: NavController, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {
            Text(
                text = "Marker",
                style = TextStyle(
                    fontFamily = gilmer,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
            }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Go Back")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun EditMarker(navController: NavController, viewModel: MainViewModel) {
    val marker by viewModel.currentMarker.observeAsState()
    var sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    val showBottomSheet by viewModel.showMarkerSaving.observeAsState(false)
    val image by viewModel.image.observeAsState(null)
    val uri by viewModel.imageUri.observeAsState(null)
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

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                viewModel.hideMarkerSaving()
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle(width = 60.dp) }
        ) {
            val markerDescription by viewModel.tempDesc.observeAsState(marker!!.description)
            val markerTitle by viewModel.tempTitle.observeAsState(marker!!.title)
            val selectedColor by viewModel.tempColor.observeAsState(marker!!.color)
            val colorOptions = listOf("Blue", "Cyan", "Green", "Yellow", "Red", "LightGray")
            val uid by viewModel.userId.observeAsState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                item {
                    Text(
                        text = "Edit marker",
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier.padding(20.dp)
                    )

                }
                item {
                    Text(
                        text = "Title",
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier.padding(20.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = markerTitle!!,
                        onValueChange = { viewModel.onTitleChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(10.dp),
                        placeholder = { Text("Marker Title (Required)") },
                        singleLine = true
                    )
                }
                item {
                    Text(
                        text = "Description",
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier.padding(20.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = markerDescription!!,
                        onValueChange = { viewModel.onDescChange(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .heightIn(max = 200.dp),
                        shape = RoundedCornerShape(10.dp),
                        placeholder = { Text("Marker Description") }
                    )
                }
                item {
                    Text(
                        text = "Marker color",
                        style = TextStyle(
                            fontFamily = gilmer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier.padding(20.dp)
                    )
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        colorOptions.forEach { item ->
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .weight(0.8f)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.onColorChange(item)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = when (item) {
                                            "Red" -> Color(0xFFff0000)
                                            "Green" -> Color(0xFF8cc63f)
                                            "Yellow" -> Color(0xFFffff00)
                                            "Blue" -> Color(0xFF0000ff)
                                            "Cyan" -> Color(0xFF29abe2)
                                            else -> Color(0xFF808080)
                                        }
                                    ),
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    shape = RoundedCornerShape(30),
                                    border = if (item == selectedColor)
                                        BorderStroke(4.dp, Color.White)
                                    else
                                        null,
                                ) {

                                }

                            }
                            if (item != colorOptions.last()) {
                                Spacer(modifier = Modifier.weight(0.3f))
                            }
                        }
                    }
                }
                item {
                    if (showPermissionDenied) {
                        PermissionDeclinedScreen()
                    } else {
                        if (image != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(top = 20.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    bitmap = image!!.asImageBitmap(),
                                    contentDescription = "PhotoTaken",
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(10)),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        } else {
                            if (marker!!.image != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(top = 20.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    GlideImage(
                                        model = marker!!.image,
                                        contentDescription = "Photo Saved",
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(10)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Button(
                            modifier = Modifier
                                .height(60.dp)
                                .width(60.dp)
                                .aspectRatio(1f),
                            contentPadding = PaddingValues(2.dp),
                            onClick = {
                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        viewModel.hideMarkerSaving()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(30)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Cancel",
                                modifier = Modifier.fillMaxSize(0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        if (image != null) {
                            Button(
                                modifier = Modifier
                                    .height(60.dp)
                                    .width(60.dp)
                                    .aspectRatio(1f),
                                contentPadding = PaddingValues(2.dp),
                                onClick = {
                                    viewModel.clearImage()
                                    navController.navigate(Routes.CameraScreen.route)
                                },
                                shape = RoundedCornerShape(30),
                                colors = ButtonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black,
                                    disabledContainerColor = Color.White,
                                    disabledContentColor = Color.Black
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddPhotoAlternate,
                                    contentDescription = "Take photo",
                                    modifier = Modifier.fillMaxSize(0.7f)
                                )
                            }
                        } else {
                            Button(
                                modifier = Modifier
                                    .height(60.dp)
                                    .aspectRatio(1f),
                                contentPadding = PaddingValues(2.dp),
                                onClick = {
                                    if (!isCameraPermissionGranted) {
                                        launcher.launch(Manifest.permission.CAMERA)
                                    } else {
                                        navController.navigate(Routes.CameraScreen.route)
                                    }
                                },
                                shape = RoundedCornerShape(30)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddAPhoto,
                                    contentDescription = "Take photo",
                                    modifier = Modifier.fillMaxSize(0.7f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth(),
                            onClick = {
                                if (uri != null) {
                                    Log.i("URI", "No nula")

                                    viewModel.addImage(
                                        uri!!, SavedMarker(
                                            marker!!.markerId,
                                            uid!!,
                                            markerTitle!!,
                                            marker!!.latitude,
                                            marker!!.longitude,
                                            selectedColor,
                                            markerDescription!!,
                                            image = null
                                        )
                                    )

                                } else {
                                    viewModel.editMarker(
                                        SavedMarker(
                                            marker!!.markerId,
                                            uid!!,
                                            markerTitle!!,
                                            marker!!.latitude,
                                            marker!!.longitude,
                                            selectedColor,
                                            markerDescription!!,
                                            marker!!.image
                                        )
                                    )
                                }
                                viewModel.clearImage()

                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        viewModel.hideMarkerSaving()
                                        viewModel.getMarker(marker!!.markerId!!)
                                    }
                                }
                            },
                            shape = RoundedCornerShape(30),
                            enabled = markerTitle != ""
                        ) {
                            Text(
                                "Edit Marker",
                                style = TextStyle(
                                    fontFamily = gilmer,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )
                        }
                    }
                }
            }

        }
    }
}