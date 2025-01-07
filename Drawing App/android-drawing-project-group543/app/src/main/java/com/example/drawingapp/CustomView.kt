package com.example.drawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * CustomView class to create a drawing canvas. Holds a bitmap and allows users to draw on the canvas
 * using onTouch function. Also sets the size of the canvas to be a square, update the brush color,
 * shape, and size, etc.
 */
class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var bitmap: Bitmap
    private lateinit var bitmapCanvas: Canvas
    private val paint = Paint()

    //width/height are 0 when the constructor is called
    //use the lazy delegated property to initialize it on first access, once the size is set
    private val rect: Rect by lazy { Rect(0, 0, width, height) }
    private var currentBrushSize: Int = 5
    private var currentColor = Color.BLACK
    private var currentBrushShape: String = "Circle"


    // Getter for the bitmap
    fun getBitmap(): Bitmap {
        return bitmap
    }

    // Setter for the bitmap
    fun setBitmap(newBitmap: Bitmap) {
        bitmap = newBitmap
        bitmapCanvas = Canvas(bitmap)
        invalidate() // Redraw when bitmap is set
    }

    // Draw on the bitmap canvas
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    //This function was written with the help of ChatGPT to make sure the canvas is square
    // and the height is the same as the width
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

    // When user touches the bitmap/canvas, draw where the user touches using their current brush
    // color, shape, and size.
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.x //GETS MOUSE COORDINATES
        val y: Float = e.y

        // If the brush shape is a circle, call drawCircle()
        if (currentBrushShape == "Circle") {
            bitmapCanvas.drawCircle(
                x, y, currentBrushSize.toFloat(), paint
            ) //CHANGE NUMBER DEPENDING ON BRUSH SIZE
        }
        // If the brush shape is a rectangle, call drawRect()
        if (currentBrushShape == "Rectangle") {
            bitmapCanvas.drawRect(
                x, y,
                x + currentBrushSize.toFloat(),
                y + currentBrushSize.toFloat(),
                paint
            )
        }
        invalidate() // Forces onDraw to be called
        return true
    }

    // Set the brush shape
    fun setBrushShape(newShape: String) {
        currentBrushShape = newShape
    }

    // Set brush size
    fun setBrushSize(newSize: Int) {
        currentBrushSize = newSize
    }

    // Set the brush color
    fun setBrushColor(newColor: Int) {
        currentColor = newColor
        paint.color = currentColor
    }

    // Update the bitmap in the viewmodel
    fun updateBitmap(drawingViewModel: DrawingViewModel) {
        drawingViewModel.updateBitmap(bitmap)
    }
}