package com.example.drawingapp

import android.graphics.Color
import androidx.lifecycle.testing.TestLifecycleOwner
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class DrawingViewModelTest {
    private lateinit var mockRepository: DrawRepository
    private lateinit var viewModelFactory: DrawingViewModelFactory
   // private val drawingViewModel = DrawingViewModel()
    private val lifecycleOwner = TestLifecycleOwner()
    @Before
    fun setup(){
        mockRepository = mock(DrawRepository::class.java)
        viewModelFactory = DrawingViewModelFactory(mockRepository)
        val drawingViewModel = viewModelFactory.create(DrawingViewModel::class.java)
    }
    @Test
    fun testNewColor() {
        val drawingViewModel = viewModelFactory.create(DrawingViewModel::class.java)
        val originalColor = drawingViewModel.observableColor.value!!
        var callbackFired = false
        drawingViewModel.observableColor.observe(lifecycleOwner) {
            callbackFired = true
        }
        drawingViewModel.newColor(Color.RED)
        assertTrue(callbackFired)
        assertEquals(Color.RED, drawingViewModel.observableColor.value)
        assertNotSame(originalColor, drawingViewModel.observableColor.value)
    }

    @Test
    fun testNewBrushSize() {
        val drawingViewModel = viewModelFactory.create(DrawingViewModel::class.java)
        val originalSize = drawingViewModel.observableSize.value!!
        var callbackFired = false
        drawingViewModel.observableSize.observe(lifecycleOwner) {
            callbackFired = true
        }
        drawingViewModel.newBrushSize(10)
        assertTrue(callbackFired)
        assertEquals(10, drawingViewModel.observableSize.value)
        assertNotSame(originalSize, drawingViewModel.observableSize.value)
    }

    @Test
    fun testNewBrushShape() {
        val drawingViewModel = viewModelFactory.create(DrawingViewModel::class.java)
        val originalShape = drawingViewModel.observableShape.value!!
        var callbackFired = false
        drawingViewModel.observableShape.observe(lifecycleOwner) {
            callbackFired = true
        }
        drawingViewModel.newBrushShape("Rectangle")
        assertTrue(callbackFired)
        assertEquals("Rectangle", drawingViewModel.observableShape.value)
        assertNotSame(originalShape, drawingViewModel.observableShape.value)
    }

    @Test
    fun testDefaultValues() {
        val drawingViewModel = viewModelFactory.create(DrawingViewModel::class.java)
        assertEquals(Color.BLACK, drawingViewModel.observableColor.value)
        assertEquals(5, drawingViewModel.observableSize.value)
        assertEquals("Circle", drawingViewModel.observableShape.value)
    }

    @Test
    fun testAllOptionChanges() {
        val drawingViewModel = viewModelFactory.create(DrawingViewModel::class.java)
        drawingViewModel.newColor(Color.GREEN)
        drawingViewModel.newBrushSize(20)
        drawingViewModel.newBrushShape("Rectangle")
        drawingViewModel.newBrushShape("Circle")
        assertEquals(Color.GREEN, drawingViewModel.observableColor.value)
        assertEquals(20, drawingViewModel.observableSize.value)
        assertEquals("Circle", drawingViewModel.observableShape.value)
    }
}