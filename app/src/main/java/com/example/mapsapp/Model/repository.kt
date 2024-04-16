package com.example.mapsapp.Model

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class repository {

    private val database = FirebaseFirestore.getInstance()

    fun addMarker(marker: SavedMarker) {
        database.collection("SavedMarkers")
            .add(
                hashMapOf(
                    "title" to marker.title,
                    "uid" to marker.uid,
                    "latitude" to marker.latitude,
                    "longitude" to marker.longitude,
                    "color" to marker.color,
                    "description" to marker.description,
                    "image" to marker.image
                )
            )
    }

    fun editMarker(editedMarker: SavedMarker) {
        database.collection("SavedMarkers").document(editedMarker.markerId!!).set(
            hashMapOf(
                "title" to editedMarker.title,
                "uid" to editedMarker.uid,
                "latitude" to editedMarker.latitude,
                "longitude" to editedMarker.longitude,
                "color" to editedMarker.color,
                "description" to editedMarker.description,
                "image" to editedMarker.image
            )
        )
    }

    fun deleteMarker(markerId: String) {
        database.collection("SavedMarkers").document(markerId).delete()
    }

    fun getMarkers(): CollectionReference {
        return database.collection("SavedMarkers")
    }

    fun getMarker(markerId: String): DocumentReference {
        return database.collection("SavedMarkers").document(markerId)
    }
}