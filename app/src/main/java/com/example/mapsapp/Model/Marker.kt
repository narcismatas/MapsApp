package com.example.mapsapp.Model

import androidx.compose.ui.graphics.Color

data class SavedMarker(
    var markerId: String? = null,
    var uid: String,
    var title: String,
    var latitude: Double,
    var longitude: Double,
    var color: String = "",
    var description: String? = null,
    var image: String?
){
    constructor(): this(null, "", "", 0.0,0.0, "Red","",null)
}