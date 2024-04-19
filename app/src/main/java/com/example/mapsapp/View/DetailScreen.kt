package com.example.mapsapp.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                            Text(
                                text = marker!!.title,
                                style = TextStyle(
                                    fontFamily = gilmer,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black
                                ),
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(bottom = 5.dp)
                            )

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
                        }

                    }
                }
                if (marker!!.description!! != "") {
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
                                Text(
                                    text = "Description",
                                    style = TextStyle(
                                        fontFamily = gilmer,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(bottom = 5.dp)
                                )

                                Text(
                                    text = "Description",
                                    style = TextStyle(
                                        fontFamily = gilmer,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(bottom = 5.dp)
                                )
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
                                GlideImage(
                                    model = marker!!.image,
                                    contentDescription = "Marker Image",
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier
                                        .wrapContentHeight()
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(25.dp)),
                                )
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
fun DetailMap(marker: SavedMarker){
    val position = LatLng(marker.latitude, marker.longitude)
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1 / 1.5f)
            .clip(RoundedCornerShape(25.dp)),
        cameraPositionState = CameraPositionState(CameraPosition(position,12f, 0f, 0f)),
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
    ){
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