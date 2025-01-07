package com.example.drawingapp

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DrawingViewModel(private val repository: DrawRepository) : ViewModel() {


    val allDrawings: LiveData<List<DrawData>> = repository.allDrawings

    var sharedImageUrl : String = ""
    private val _showPopup = MutableStateFlow(false)
    val showPopup: StateFlow<Boolean> = _showPopup


    // Store LiveData variables for the brush color, size, shape, and bitmap
    private var currentColor = MutableLiveData<Int>(Color.BLACK)
    private var currentBrushSize = MutableLiveData<Int>(5)
    private var currentBrushShape = MutableLiveData<String>("Circle")
    private var bitmap = MutableLiveData<Bitmap>(
        Bitmap.createBitmap(
            Resources.getSystem().displayMetrics.widthPixels,
            Resources.getSystem().displayMetrics.widthPixels,
            Bitmap.Config.ARGB_8888
        )
    )

    val observableBitmap: LiveData<Bitmap> = bitmap as LiveData<Bitmap>
    val observableColor = currentColor as LiveData<Int>
    val observableSize = currentBrushSize as LiveData<Int>
    val observableShape = currentBrushShape as LiveData<String>


    fun getDownloadUrl(): String {
        return sharedImageUrl
    }
    fun showDialog() {
        _showPopup.value = true
    }

    fun dismissDialog() {
        _showPopup.value = false
    }

    fun getPopup(): Boolean {
        return _showPopup.value
    }


    // Update the stored bitmap so we can access it when the screen is rotated
    fun updateBitmap(newBitmap: Bitmap) {
        bitmap.value = newBitmap
    }

    // Update the brush color
    fun newColor(color: Int) {
        currentColor.value = color
    }

    // Update the brush size
    fun newBrushSize(newSize: Int) {
        currentBrushSize.value = newSize
    }

    // Update the brush shape
    fun newBrushShape(newShape: String) {
        currentBrushShape.value = newShape
    }

    // Save the current bitmap to the repository
    fun saveImage(name: String) {
        val currentBitmap = bitmap.value
        if (currentBitmap != null) {
            viewModelScope.launch {
                val exists = repository.checkDrawingInDatabase(name)
                if (exists) {
                    // If the drawing exists, update the image in the DB
                    repository.updatePic(name, currentBitmap)
                } else {
                    // If the drawing doesn't exist, add a new image to the DB
                    repository.addPic(name, currentBitmap)
                }
            }
        } else {
            Log.e("ViewModel", "No bitmap to save!")
        }
    }

//    Upload the current bitmap to the Firebase storage. If successful, a popup will appear allowing the user to copy the url so they can send it to other users to download
    suspend fun uploadData(): Boolean {
        //val fileRef = ref.child(path)
        //val storageReference = FirebaseStorage.getInstance().getReference().child("${Firebase.auth.currentUser}/725.png")
        val storageReference =  Firebase.storage.reference.child("${Firebase.auth.currentUser}/${System.currentTimeMillis()}.png")
        val baos = ByteArrayOutputStream()
        bitmap.value!!.compress(Bitmap.CompressFormat.PNG, 0, baos) //save it into PNG format (in memory, not a file)
        return suspendCoroutine { continuation ->
            val uploadTask = storageReference.putBytes(baos.toByteArray())
            uploadTask
                .addOnFailureListener { e ->
                    Log.e("PICUPLOAD", "Failed !$e")
                    continuation.resume(false)
                }
                .addOnSuccessListener {
                    Log.d("PICUPLOAD", "success")
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        // The download URL is available in uri
                         sharedImageUrl = uri.toString()
                        Log.d("DownloadURL", "Download URL: $sharedImageUrl")
                    }.addOnFailureListener { exception ->
                        // Handle any errors here
                        Log.e("DownloadURL", "Failed to get download URL", exception)
                    }
                    continuation.resume(true)
                }
        }
    }

    fun downloadImageFromUrl(url: String, name: String) {
        viewModelScope.launch {
            try {
                val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url)

                // Download the image as bytes
                val bytes = withContext(Dispatchers.IO) {
                    storageReference.getBytes(10 * 1024 * 1024).await()
                }

                // Decode the image bytes directly into a bitmap
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                if (bitmap != null) {
                    repository.addPic(name, bitmap)
                } else {
                    Log.e("DownloadImage", "Failed to decode bitmap")
                }
            } catch (e: Exception) {
                Log.e("DownloadImage", "Error downloading image: ${e.message}")
            }
        }
    }


    // Remove the drawing specified by the ID
    fun deleteDrawing(drawingId: Int) {
        viewModelScope.launch {
            repository.deletePicById(drawingId)
        }
    }
}

// This factory class allows us to define custom constructors for the view model
class DrawingViewModelFactory(private val repository: DrawRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DrawingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DrawingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
