package com.example.mapsapp.View

sealed class Routes(val route: String) {

    object HomeScreen : Routes("home_screen")
    object ListScreen: Routes("list_screen")
    object CameraScreen: Routes("camera_screen")
    object GalleryScreen: Routes("gallery_screen")
    object LogInScreen: Routes("login_screen")
    object RegisterScreen: Routes("register_screen")
    object DetailScreen: Routes("detail_screen")

}