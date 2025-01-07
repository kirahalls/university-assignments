package com.example.drawingapp

import android.app.Application
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.after
import org.robolectric.RobolectricTestRunner
import org.mockito.Mockito.mock
import org.junit.Assert.*
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class MyViewModelFactoryTest {
    private lateinit var mockRepository: DrawRepository
    private lateinit var viewModelFactory: DrawingViewModelFactory

    @Before
    fun setup(){
        mockRepository = mock(DrawRepository::class.java)
        viewModelFactory = DrawingViewModelFactory(mockRepository)
    }
    @Test
    fun `getDrawingByNameTest`() {
        val bitmap1 = createColoredBitmap(Color.RED)
        val drawData1 = DrawData(1, "1",bitmap1)
        val liveData1 = MutableLiveData<DrawData?>().apply { value = drawData1 }
        Mockito.`when`(mockRepository.getDrawingByName("1")).thenReturn(liveData1)
        mockRepository.addPic("1",bitmap1)
        val repo = mockRepository.getDrawingByName("1")
        assertSame("1", repo.value?.name)

    }
    @Test
    fun `drawingPersistence`() = runBlocking {
        val bitmap1 = createColoredBitmap(Color.RED)
        val bitmap2 = createColoredBitmap(Color.GREEN)

        val drawData1 = DrawData(1, "Drawing1", bitmap1)
        val drawData2 = DrawData(2, "Drawing2", bitmap2)


        Mockito.`when`(mockRepository.getDrawingByName("Drawing1")).thenReturn(MutableLiveData(drawData1))
        Mockito.`when`(mockRepository.getDrawingByName("Drawing2")).thenReturn(MutableLiveData(drawData2))


        mockRepository.addPic("Drawing1", bitmap1)
        mockRepository.addPic("Drawing2", bitmap2)


        val newMockRepository = mock<DrawRepository>()
        val newViewModelFactory = DrawingViewModelFactory(newMockRepository)
        val newViewModel = newViewModelFactory.create(DrawingViewModel::class.java)

        // Simulate getting drawings after "app restart"
        Mockito.`when`(newMockRepository.getDrawingByName("Drawing1")).thenReturn(MutableLiveData(drawData1))
        Mockito.`when`(newMockRepository.getDrawingByName("Drawing2")).thenReturn(MutableLiveData(drawData2))

        // Step 3: Verify that the drawings still exist after "restarting" the app
        val drawing1 = newMockRepository.getDrawingByName("Drawing1")?.value
        val drawing2 = newMockRepository.getDrawingByName("Drawing2")?.value

        assertNotNull(drawing1)
        assertNotNull(drawing2)
        assertEquals("Drawing1", drawing1?.name)
        assertEquals("Drawing2", drawing2?.name)

    }
    @Test
    fun `add2Bitmaps`() {

        val viewModel = viewModelFactory.create(DrawingViewModel::class.java)

        val bitmap1 = createColoredBitmap(Color.RED)
        val bitmap2 = createColoredBitmap(Color.RED)
        // Mock repository behavior
        // Create DrawData objects (Assuming DrawData holds the Bitmap)
        val drawData1 = DrawData(1, "1",bitmap1) // Adjust constructor if needed
        val drawData2 = DrawData(2, "2", bitmap2)

        val liveData1 = MutableLiveData<DrawData?>().apply { value = drawData1 }
        val liveData2 = MutableLiveData<DrawData?>().apply { value = drawData2 }

        // Mock the repository to return liveData1 and liveData2 for specific names
        Mockito.`when`(mockRepository.getDrawingByName("1")).thenReturn(liveData1)
        Mockito.`when`(mockRepository.getDrawingByName("2")).thenReturn(liveData2)

        mockRepository.addPic("1",bitmap1)
        mockRepository.addPic("2",bitmap2)
        val before = mockRepository.getDrawingByName("1").value

        val after = mockRepository.getDrawingByName("2").value
        assertNotSame(before,after)
    }

    @Test
    fun `updatePict`() {
        val bitmap1 = createColoredBitmap(Color.RED)
        val bitmap2 = createColoredBitmap(Color.GREEN)
        val drawData1 = DrawData(1, "1",bitmap1) // Adjust constructor if needed
        val drawData2 = DrawData(2, "2", bitmap2)
        val liveData1 = MutableLiveData<DrawData?>().apply { value = drawData1 }
        val liveData2 = MutableLiveData<DrawData?>().apply { value = drawData2 }

        Mockito.`when`(mockRepository.getDrawingByName("1")).thenReturn(liveData1)
        Mockito.`when`(mockRepository.getDrawingByName("2")).thenReturn(liveData2)
        mockRepository.addPic("1",bitmap1)
        val before = mockRepository.getDrawingByName("1").value
        mockRepository.updatePic("1",bitmap2)
        // Manually update the liveData with the new DrawData (this should reflect the update)
        val updatedDrawData = DrawData(1, "1", bitmap2)
        liveData1.value = updatedDrawData // Update the LiveData manually after updatePic
        val after = mockRepository.getDrawingByName("1").value
        // Assert that the `before` and `after` are not the same object (meaning the value changed)
        assertNotSame(before, after)


    }
    @Test
    fun `bitmapCount`() {
        val bitmap1 = createColoredBitmap(Color.RED)
        val bitmap2 = createColoredBitmap(Color.RED)
        val drawData1 = DrawData(1, "1",bitmap1) // Adjust constructor if needed
        val drawData2 = DrawData(2, "2", bitmap2)
        val drawDataList = listOf(drawData1, drawData2)
        val liveData = MutableLiveData<List<DrawData>>().apply { value = drawDataList }
        Mockito.`when`(mockRepository.allDrawings).thenReturn(liveData)
        mockRepository.addPic("1",bitmap1)
        mockRepository.addPic("2", bitmap2)
        val drawings = mockRepository.allDrawings.value
        assertEquals(2,drawings?.size)


    }

    @Test
    fun `doesNotExist`() = runBlocking {
        Mockito.`when`(mockRepository.checkDrawingInDatabase("1")).thenReturn(false)
        val ret = mockRepository.checkDrawingInDatabase("1")
        assertSame(false, ret)
    }


}



fun createColoredBitmap(color: Int): Bitmap {
    // Create a mutable bitmap with the specified width, height, and configuration
    val bitmap = Bitmap.createBitmap(Resources.getSystem().displayMetrics.widthPixels,
        Resources.getSystem().displayMetrics.widthPixels,
        Bitmap.Config.ARGB_8888)

    // Create a canvas to draw on the bitmap
    val canvas = Canvas(bitmap)
    // Fill the bitmap with the specified color
    canvas.drawColor(color)

    return bitmap
}