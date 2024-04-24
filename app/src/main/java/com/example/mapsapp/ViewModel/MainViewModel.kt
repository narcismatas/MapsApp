package com.example.mapsapp.ViewModel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.Model.repository
import com.example.mapsapp.Model.SavedMarker
import com.example.mapsapp.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//Font
val gilmer = FontFamily(
    Font(R.font.gilmer_bold, FontWeight.Bold),
    Font(R.font.gilmer_heavy, FontWeight.Black),
    Font(R.font.gilmer_medium, FontWeight.Medium),
    Font(R.font.gilmer_light, FontWeight.Light),
    Font(R.font.gilmer_outline, FontWeight.Thin),
    Font(R.font.gilmer_regular, FontWeight.Normal)
)



class MainViewModel: ViewModel(){

    private val repository = repository()

    private val auth = FirebaseAuth.getInstance()

    private val _selectedPosition = MutableLiveData(LatLng(0.0, 0.0))
    val selectedPosition = _selectedPosition

    private val _tempTitle = MutableLiveData("")
    val tempTitle = _tempTitle

    private val _tempDesc = MutableLiveData("")
    val tempDesc = _tempDesc

    private val _tempColor = MutableLiveData("Blue")
    val tempColor = _tempColor

    private val _showMarkerSaving = MutableLiveData(false)
    val showMarkerSaving = _showMarkerSaving

    private val _isCurrentLocation = MutableLiveData(false)
    val isCurrentLocation = _isCurrentLocation

    private val _markers = MutableLiveData<MutableList<SavedMarker>?>(null)
    val markers = _markers

    private val _currentMarker = MutableLiveData(SavedMarker())
    val currentMarker = _currentMarker

    private val _image = MutableLiveData<Bitmap?>()
    val image = _image

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri = _imageUri

    private val _imageUrl = MutableLiveData<String?>()
    val imageUrl = _imageUrl

    private val _cameraPermissionGranted = MutableLiveData(false)
    val cameraPermissionGranted = _cameraPermissionGranted

    private val _shouldShowPermissionRationale = MutableLiveData(false)
    val shouldShowPermissionRationale = _shouldShowPermissionRationale

    private val _showPermissionDenied = MutableLiveData(false)
    val showPermissionDenied = _showPermissionDenied

    private val _showLocPermissionDenied = MutableLiveData(false)
    val showLocPermissionDenied = _showLocPermissionDenied

    private val _goToNext = MutableLiveData<Boolean>(false)
    val goToNext = _goToNext

    private val _showLoading = MutableLiveData(false)
    val showLoading = _showLoading

    private val _userId = MutableLiveData<String>()
    val userId = _userId

    private val _loggedUser = MutableLiveData<String>()
    val loggedUser = _loggedUser

    private val _showToastUnknownUser = MutableLiveData(false)
    val showToastUnknownUser = _showToastUnknownUser

    private val _showToastExistentUser = MutableLiveData(false)
    val showToastExistentUser = _showToastExistentUser

    private val _colorFilter = MutableLiveData("")
    val colorFilter = _colorFilter

    private val _showDeleteDialog = MutableLiveData(false)
    val showDeleteDialog = _showDeleteDialog

    private val _deleteId = MutableLiveData("")
    val deleteId = _deleteId

    fun setCameraPermissionGranted(granted: Boolean) {
        _cameraPermissionGranted.value = granted
    }

    fun setShouldPermissionRationale(should: Boolean){
        _shouldShowPermissionRationale.value = should
    }

    fun setShowPermissionDenied(denied: Boolean){
        _showPermissionDenied.value = denied
    }

    fun setShowLocPermissionDenied(denied: Boolean){
        _showLocPermissionDenied.value = denied
    }


    fun onColorChange(color: String){
        _tempColor.value = color
    }

    fun onTitleChange(title: String){
        _tempTitle.value = title
    }

    fun onDescChange(desc: String){
        _tempDesc.value = desc
    }

    fun newPosSelected (latLng: LatLng, isCurrentLocation: Boolean){
        _selectedPosition.value = latLng
        _showMarkerSaving.value = true
        _isCurrentLocation.value = isCurrentLocation
    }

    fun showEditMarker(title: String, desc: String, color: String){
        _showMarkerSaving.value = true
        _tempTitle.value = title
        _tempDesc.value = desc
        _tempColor.value = color
    }

    fun hideMarkerSaving(){
        _showMarkerSaving.value = false
        _image.value = null
        _imageUri.value = null
        _tempTitle.value = ""
        _tempDesc.value = ""
        _tempColor.value = "Blue"
    }

    fun addMarker(info: SavedMarker){
        repository.addMarker(info)
        getMarkers()
    }



    fun photoTaken(image: Bitmap, imageUri: Uri?){
        _image.value = image
        _imageUri.value = imageUri
    }

    fun clearImage(){
        _image.value = null
        _imageUri.value = null
        _imageUrl.value = null
    }

    fun getMarkers(){
        repository.getMarkers().whereEqualTo("uid", userId.value).addSnapshotListener{value, error ->
            if (error != null){
                Log.e("Firestone error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<SavedMarker>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newMarker = dc.document.toObject(SavedMarker::class.java)
                    newMarker.markerId = dc.document.id
                    tempList.add(newMarker)
                }
            }
            _markers.value = tempList
        }
    }

    fun getFilteredMarkers(){
        repository.getMarkers().whereEqualTo("uid", userId.value).whereEqualTo("color", colorFilter.value).addSnapshotListener{value, error ->
            if (error != null){
                Log.e("Firestone error", error.message.toString())
                return@addSnapshotListener
            }
            val tempList = mutableListOf<SavedMarker>()
            for (dc: DocumentChange in value?.documentChanges!!) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    val newMarker = dc.document.toObject(SavedMarker::class.java)
                    newMarker.markerId = dc.document.id
                    tempList.add(newMarker)
                }
            }
            _markers.value = tempList
        }
    }

    fun getMarker(markerId: String): SavedMarker? {
        repository.getMarker(markerId).addSnapshotListener { value, error ->
            if (error != null){
                Log.w("UserRepository", "Listen failed", error)
                return@addSnapshotListener
            }
            if (value != null && value.exists()){
                val marker = value.toObject(SavedMarker::class.java)
                if (marker != null){
                    marker.markerId = markerId
                }
                _currentMarker.value = marker
            } else {
                Log.d("UserRepository", "Current data: Null")
            }
        }

        return currentMarker.value
    }



    fun uploadImage(imageUri: Uri, info: SavedMarker){
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        storage.putFile(imageUri)
            .addOnSuccessListener {
                Log.i("IMAGE UPLOAD", "Image uploaded successfully")
                storage.downloadUrl.addOnSuccessListener {
                    Log.i("IMAGEN", it.toString())
                    addMarker(
                        SavedMarker(
                            null,
                            info.uid,
                            info.title,
                            info.latitude,
                            info.longitude,
                            info.color,
                            info.description,
                            image = it.toString()
                        )
                    )
                }
            }
            .addOnFailureListener {
                Log.i("IMAGE UPLOAD", "Image upload failed")
                addMarker(
                    SavedMarker(
                        null,
                        info.uid,
                        info.title,
                        info.latitude,
                        info.longitude,
                        info.color,
                        info.description,
                        image = null
                    )
                )
            }

    }

    fun addImage(imageUri: Uri, info: SavedMarker){
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storage = FirebaseStorage.getInstance().getReference("images/$fileName")
        storage.putFile(imageUri)
            .addOnSuccessListener {
                Log.i("IMAGE UPLOAD", "Image uploaded successfully")
                storage.downloadUrl.addOnSuccessListener {
                    Log.i("IMAGEN", it.toString())
                    repository.editMarker(
                        SavedMarker(
                            info.markerId,
                            info.uid,
                            info.title,
                            info.latitude,
                            info.longitude,
                            info.color,
                            info.description,
                            image = it.toString()
                        )
                    )
                    getMarkers()
                }
            }
            .addOnFailureListener {
                Log.i("IMAGE UPLOAD", "Image upload failed")
                repository.editMarker(
                    SavedMarker(
                        info.markerId,
                        info.uid,
                        info.title,
                        info.latitude,
                        info.longitude,
                        info.color,
                        info.description,
                        image = null
                    )
                )
                getMarkers()
            }
    }

    fun editMarker(info: SavedMarker){
        repository.editMarker(info)
    }



    fun modifyProcessing(){
        _showLoading.value = !(_showLoading.value)!!
    }

    fun register(username: String, password: String){
        modifyProcessing()
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    login(username, password, true)
                } else{
                    _goToNext.value = false

                }
            }
            .addOnFailureListener {
                _showToastExistentUser.value = true
                modifyProcessing()
            }
    }

    fun login(username: String?, password: String?, register: Boolean){
        if (!register){
            modifyProcessing()
        }
        auth.signInWithEmailAndPassword(username!!, password!!)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    _userId.value = task.result.user?.uid
                    _loggedUser.value = task.result.user?.email?.split("@")?.get(0)
                    _goToNext.value = true
                } else {
                    _goToNext.value = false
                }

            }
            .addOnFailureListener{
                _showToastUnknownUser.value = true
                _showLoading.value = false
            }
    }

    fun hideToast(){
        _showToastUnknownUser.value = false
        _showToastExistentUser.value = false
        _goToNext.value = false
        _showLoading.value = false
    }

    fun logout(){
        auth.signOut()
        _goToNext.value = false
    }

    fun changeColorFilter(color: String){
        _colorFilter.value = color
    }

    fun deleteMarker(){
        repository.deleteMarker(deleteId.value!!)
    }

    fun showDeleteDialog(markerId: String?){
        if (_showDeleteDialog.value!!) _deleteId.value = "" else _deleteId.value = markerId
        _showDeleteDialog.value = !showDeleteDialog.value!!
    }
}