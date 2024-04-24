package com.example.mapsapp.View

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.mapsapp.ViewModel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date


@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun CameraScreen(navController: NavController, viewModel: MainViewModel) {

    val image by viewModel.image.observeAsState()
    val context = LocalContext.current
    var uri: Uri? by remember { mutableStateOf(null) }
    val controller = remember {
        LifecycleCameraController(context).apply {
            CameraController.IMAGE_CAPTURE
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (image == null) {
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 10.dp)
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(3))

            )
            FloatingActionButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset((-16).dp, 26.dp),
                containerColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Switch camera",
                    tint = Color.Black
                )
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.GalleryScreen.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(16.dp, (-16).dp)
                    .size(50.dp),
                containerColor = Color.White

            ) {
                Icon(
                    imageVector = Icons.Default.ImageSearch,
                    contentDescription = "Select From Gallery",
                    tint = Color.Black,
                    modifier = Modifier.size(40.dp)
                )
            }
            FloatingActionButton(
                onClick = {
                    takePhoto(context, controller) { photo ->
                        uri = bitmapToUri(context, photo)
                        Log.i("IMAGEN",uri.toString())
                        viewModel.photoTaken(photo, uri)

                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(0.dp, (-16).dp)
                    .size(75.dp),
                containerColor = Color.White,
                shape = CircleShape,

                ) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Take photo",
                    tint = Color.Black,
                    modifier = Modifier.size(50.dp)
                )
            }

        } else {
            Image(
                bitmap = image!!.asImageBitmap(),
                contentDescription = "PhotoTaken",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 10.dp)
                    .aspectRatio(9f / 16f)
                    .clip(RoundedCornerShape(3)),
                contentScale = ContentScale.Crop
            )
            FloatingActionButton(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset((-20).dp, (-20).dp)
                    .fillMaxHeight(0.06f)
                    .aspectRatio(1f),
                containerColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Take photo",
                    tint = Color.Black,
                    modifier = Modifier.size(50.dp)
                )
            }
            FloatingActionButton(
                onClick = {
                    viewModel.clearImage()
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset((20).dp, (-20).dp)
                    .fillMaxHeight(0.06f)
                    .aspectRatio(1f),
                containerColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel image",
                    tint = Color.Black,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        FloatingActionButton(
            onClick = {
                viewModel.clearImage()
                navController.navigateUp()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(16.dp, 26.dp),
            containerColor = Color.White
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Go back",
                tint = Color.Black
            )
        }


    }


}

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        }, modifier = modifier
    )
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )
                onPhotoTaken(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Error taken photo", exception)
            }
        }
    )
}

fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val filename = "${System.currentTimeMillis()}.jpg"
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, filename)
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
        put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    }

    val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    uri?.let {
        val outstream: OutputStream? = context.contentResolver.openOutputStream(it)
        outstream?.let { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        outstream?.close()
    }
    return uri
}