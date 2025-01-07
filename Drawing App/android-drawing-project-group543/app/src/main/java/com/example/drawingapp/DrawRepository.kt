package com.example.drawingapp

import android.graphics.Bitmap
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DrawRepository(
    private val scope: CoroutineScope,
    private val dao: DrawDAO
) {

    // Add a new drawing
    fun addPic(name: String, pic: Bitmap) {
        scope.launch {
            val newDraw = DrawData(name = name, pict = pic)  // ID will be auto-generated
            dao.addPictData(newDraw)
        }
    }

    // Update an existing drawing
    fun updatePic(name: String, updatedBitmap: Bitmap) {
        scope.launch {
            // First, get the drawing by name
            val drawing = dao.getDrawingByName(name)
            drawing.collect { existingDrawing ->
                // If drawing exists, update the bitmap
                val updatedDraw = existingDrawing?.apply {
                    pict = updatedBitmap
                }
                if (updatedDraw != null) {
                    dao.updatePictData(updatedDraw)
                }
            }
        }
    }

    // Remove image from database
    fun deletePicById(drawingId: Int) {
        scope.launch {
            dao.deletePictDataById(drawingId)
        }
    }

    // Get a drawing by name
    fun getDrawingByName(name: String) = dao.getDrawingByName(name).asLiveData()

    // Check if the drawing exists in the database
    suspend fun checkDrawingInDatabase(name: String) = dao.checkDrawingInDatabase(name)

    // Get all drawings
    val allDrawings = dao.getAllDrawings().asLiveData()
}
