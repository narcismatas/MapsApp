package com.example.mapsapp.View

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import com.example.mapsapp.ViewModel.MainViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.mapsapp.R
import com.example.mapsapp.ViewModel.gilmer

@Composable
fun GalleryScreen(navController: NavController, viewModel: MainViewModel){
    val context = LocalContext.current
    val img: Bitmap? = ContextCompat.getDrawable(context, R.drawable.no_image_selected)?.toBitmap()
    var uri: Uri? by remember { mutableStateOf(null) }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            bitmap = if (Build.VERSION.SDK_INT < 28){
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = it?.let { it1->
                    ImageDecoder.createSource(context.contentResolver, it1)
                }
                source?.let { it1 ->
                    ImageDecoder.decodeBitmap(it1)
                }
            }
            uri = it
        }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            bitmap = if (bitmap != null) bitmap!!.asImageBitmap() else img!!.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .fillMaxWidth(0.9f)
                .background(Color.Black)
                .align(Alignment.Center)
        )
        Button(
            onClick = { launchImage.launch("image/*") },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 16.dp),
            shape = RoundedCornerShape(35)
        ) {
            Text(text = "Open Gallery", style = TextStyle(fontFamily = gilmer, fontSize = 24.sp, fontWeight = FontWeight.Bold))
        }
        Button(
            onClick = {
                viewModel.photoTaken(bitmap!!, uri)
                navController.navigateUp()
                navController.navigateUp()
                      },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-16).dp),
            shape = RoundedCornerShape(35),
            enabled = bitmap != null
        ) {
            Text(text = "Save selection", style = TextStyle(fontFamily = gilmer, fontSize = 24.sp, fontWeight = FontWeight.Bold))
        }
    }
}