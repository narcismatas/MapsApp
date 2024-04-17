package com.example.mapsapp.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.mapsapp.MainActivity
import com.example.mapsapp.Model.SavedMarker
import com.example.mapsapp.R
import com.example.mapsapp.ViewModel.MainViewModel
import com.example.mapsapp.ViewModel.gilmer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel, state: DrawerState) {
    HomeScaffold(navController, viewModel, state)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScaffold(navController: NavController, viewModel: MainViewModel, state: DrawerState) {
    Scaffold(
        topBar = { MyTopAppBar(viewModel, state) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            val permissionState =
                rememberPermissionState(permission = android.Manifest.permission.ACCESS_FINE_LOCATION)
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }
            if (permissionState.status.isGranted) {
                Map(viewModel)
            } else {
                Box(modifier = Modifier.padding(it)) {
                    Text(text = "Permissions required")
                    Button(onClick = { permissionState.launchPermissionRequest() }) {
                        Text(text = "Accept")
                    }
                }
            }
            AddLocation(navController, viewModel = viewModel)

        }

    }

}

@SuppressLint("MissingPermission")
@Composable
fun Map(viewModel: MainViewModel) {
    val context = LocalContext.current
    val fusedLocationProvidedClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }
    var lastKnownLocation by remember { mutableStateOf<Location?>(null) }
    var deviceLatLng by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    val cameraPositionState =
        rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(deviceLatLng, 14f) }
    val locationResult = fusedLocationProvidedClient.getCurrentLocation(100, null)
    locationResult.addOnCompleteListener(context as MainActivity) { task ->
        if (task.isSuccessful) {
            lastKnownLocation = task.result
            deviceLatLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
            cameraPositionState.position = CameraPosition.fromLatLngZoom(deviceLatLng, 14f)
        } else {
            Log.e("Error", "Exception: %s", task.exception)
        }
    }

    val selectedPos by viewModel.selectedPosition.observeAsState(LatLng(0.0, 0.0))
    Box {
        if (lastKnownLocation != null) {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.newPosSelected(
                        LatLng(
                            lastKnownLocation!!.latitude,
                            lastKnownLocation!!.longitude
                        ), true
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .zIndex(100f)
                    .offset(x = (-30).dp, y = (-30).dp)
                    .height(50.dp)
                    .wrapContentWidth(),
                containerColor = Color.Black,
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Add marker to location",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Your Location",
                    style = TextStyle(
                        fontFamily = gilmer,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    compassEnabled = true,
                    tiltGesturesEnabled = true,
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = false
                ),
                properties = MapProperties(
                    isBuildingEnabled = true,
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = true,
                ),
                onMapLongClick = {
                    viewModel.newPosSelected(it, false)
                    println("" + selectedPos.latitude + " " + selectedPos.longitude)
                }

            ) {
                val markers by viewModel.markers.observeAsState()
                viewModel.getMarkers()
                markers?.forEach {
                    val icon = when (it.color) {
                        "Red" -> R.drawable.red_marker
                        "Green" -> R.drawable.green_marker
                        "Yellow" -> R.drawable.yellow_marker
                        "Cyan" -> R.drawable.cyan_marker
                        "Blue" -> R.drawable.blue_marker
                        else -> R.drawable.gray_marker
                    }
                    Marker(
                        state = MarkerState(LatLng(it.latitude, it.longitude)),
                        draggable = false,
                        title = it.title,
                        snippet = if (it.description!!.isNotBlank()) it.description else null,
                        icon = BitmapDescriptorFactory.fromResource(icon)
                    )
                }

            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocation(navController: NavController, viewModel: MainViewModel) {
    val sheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()
    val showBottomSheet by viewModel.showMarkerSaving.observeAsState(false)
    val isCurrentLocation by viewModel.isCurrentLocation.observeAsState(true)
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
                    shouldShowRequestPermissionRationale(
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
            val markerDescription by viewModel.tempDesc.observeAsState()
            val markerTitle by viewModel.tempTitle.observeAsState()
            val selectedColor by viewModel.tempColor.observeAsState("Red")
            val colorOptions = listOf("Blue", "Cyan", "Green", "Yellow", "Red", "LightGray")
            val uid by viewModel.userId.observeAsState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                item {
                    if (isCurrentLocation) {
                        Text(
                            text = "Save current location as",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black
                            ),
                            modifier = Modifier.padding(20.dp)
                        )
                    } else {
                        Text(
                            text = "Save selected location as",
                            style = TextStyle(
                                fontFamily = gilmer,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black
                            ),
                            modifier = Modifier.padding(20.dp)
                        )
                    }
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
                            .padding(horizontal = 20.dp),
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
                    } else if (image != null) {
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
                                    viewModel.uploadImage(
                                        uri!!, SavedMarker(
                                            null,
                                            uid!!,
                                            markerTitle!!,
                                            viewModel.selectedPosition.value!!.latitude,
                                            viewModel.selectedPosition.value!!.longitude,
                                            selectedColor,
                                            markerDescription!!,
                                            image = null
                                        )
                                    )
                                } else {
                                    viewModel.addMarker(
                                        SavedMarker(
                                            null,
                                            uid!!,
                                            markerTitle!!,
                                            viewModel.selectedPosition.value!!.latitude,
                                            viewModel.selectedPosition.value!!.longitude,
                                            selectedColor,
                                            markerDescription!!,
                                            image = null
                                        )
                                    )
                                }
                                viewModel.clearImage()


                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                    if (!sheetState.isVisible) {
                                        viewModel.hideMarkerSaving()
                                    }
                                }
                            },
                            shape = RoundedCornerShape(30),
                            enabled = markerTitle != ""
                        ) {
                            Text(
                                "Add Marker",
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

@Composable
fun PermissionDeclinedScreen() {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Permission required",
            fontFamily = gilmer,
            fontWeight = FontWeight.Black,
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "This app needs access to the camera to take photos",
            fontFamily = gilmer,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier
                .height(40.dp)
                .wrapContentWidth(),
            shape = RoundedCornerShape(30),
            onClick = {
                openAppSettings(context as Activity)
            }) {
            Text(
                text = "Accept",
                fontFamily = gilmer,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
    }
}

fun openAppSettings(activity: Activity) {
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", activity.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    activity.startActivity(intent)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(viewModel: MainViewModel, state: DrawerState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = {
            Text(
                text = "My Map",
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
        }
    )
}






















