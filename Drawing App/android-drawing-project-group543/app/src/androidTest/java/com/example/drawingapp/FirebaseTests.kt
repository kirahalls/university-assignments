package com.example.drawingapp

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.compose.ui.graphics.Canvas
import android.graphics.Paint
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storage
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random
import kotlinx.coroutines.*
import org.mockito.Mockito.mock


@RunWith(AndroidJUnit4::class)
class FirebaseTests {

    /*   private val mockRepository = mock(DrawRepository::class.java)
       private val viewModel = DrawingViewModel(mockRepository)
       val mockDao = mock(DrawDAO::class.java)*/
    val userEmail = "testUser@gmail.com"
    val userPassword = "password12345"

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            //10.0.2.2 on the device emulator means your laptop
            //     Firebase.auth.useEmulator("10.0.2.2", 9099)


            val userEmail = "testUser@gmail.com"
            val userPassword = "password12345"

            //make sure we have an account and we're logged int

            runBlocking {
                suspendCoroutine<Unit> { continuation ->
                    Firebase.auth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnSuccessListener {
                            Log.d("scs", "testAuth: ")
                            continuation.resume(Unit)
                        }
                        .addOnFailureListener { err ->
                            Log.d("test", "ignoring duplicate account erro ${err}")
                            continuation.resume(Unit)
                        }
                }
            }
            runBlocking {
                suspendCoroutine<Unit> { continuation ->
                    Log.d("login", "testAuth: logging in")
                    Firebase.auth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnSuccessListener {
                            Log.d("suser", "testAuth: ")
                            continuation.resume(Unit)
                        }
                        .addOnFailureListener { err ->
                            throw Exception(err)
                        }
                }
            }
        }
    }

    @Test
    fun testImageUpload() {
        Log.d("USER", "User: ${Firebase.auth.currentUser!!.email} ${Firebase.auth.currentUser}")
        val path = "${Firebase.auth.currentUser}/picture.png"
        runBlocking {
            val data = generateBitmap()

            // Assert that uploadData returns true
            val uploadResult = uploadData(data)
            assertTrue(uploadResult)
        }
    }

    /*    @Test
        fun testImageDownload() {
            Log.d("USER", "User: ${Firebase.auth.currentUser!!.email} ${Firebase.auth.currentUser}")
            val path = "${Firebase.auth.currentUser}/picture.png"

            runBlocking {
                val downloadedData = viewModel.downloadImageFromUrl("https://firebasestorage.googleapis.com/v0/b/drawingapp-6e18e.firebasestorage.app/o/com.google.firebase.auth.internal.zzad%4039d7f83%2F1733630237854.png?alt=media&token=20ef1148-8086-403a-9818-0eabd0857f26","pic")
                assertEquals(mockDao.getDrawingByName("pic"), downloadedData)
            }
        }*/
    @Test
    fun testLoginSuccess() = runBlocking {

        val result = suspendCoroutine<Boolean> { continuation ->
            Log.d("login", "testAuth: logging in")

            Firebase.auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener {
                    Log.d("login", "testAuth: login successful")
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    Log.e("login", "testAuth: login failed", exception)
                    continuation.resume(false)
                }
        }

        // Assert the result after Firebase operation completes
        assertTrue("Login should be successful", result)
    }
    @Test
    fun testLoginFail() = runBlocking {
        // Generate random email and password (assuming these are unlikely to exist)
        val randomEmail = "invalidUser${Random.nextInt(1000, 9999)}@gmail.com"
        val randomPassword = "wrongpassword${Random.nextInt(1000, 9999)}"

        val result = suspendCoroutine<Boolean> { continuation ->
            Log.d("login", "testAuth: attempting login with email: $randomEmail")

            Firebase.auth.signInWithEmailAndPassword(randomEmail, randomPassword)
                .addOnSuccessListener {
                    Log.e("login", "testAuth: login succeeded unexpectedly")
                    continuation.resume(false) // Login succeeded, but we expect it to fail
                }
                .addOnFailureListener { exception ->
                    Log.d("login", "testAuth: login failed as expected", exception)
                    continuation.resume(true) // Login failed as expected
                }
        }
        assertTrue("Login should fail with non-existing credentials", result)
    }

    @Test
    fun testCreateUserSuccess() = runBlocking {
        val randomEmail = "testUser${Random.nextInt(1000, 9999)}@gmail.com"
        val randomPassword = "password${Random.nextInt(1000, 9999)}"

        val result = suspendCoroutine<Boolean> { continuation ->
            Log.d("createUser", "testAuth: creating user with email: $randomEmail")

            Firebase.auth.createUserWithEmailAndPassword(randomEmail, randomPassword)
                .addOnSuccessListener {
                    Log.d("createUser", "testAuth: user creation succeeded")
                    continuation.resume(true) // User creation succeeded as expected
                }
                .addOnFailureListener { exception ->
                    Log.e("createUser", "testAuth: user creation failed unexpectedly", exception)
                    continuation.resume(false) // User creation failed, which is not expected
                }
        }
        assertTrue("User creation should succeed with unique credentials", result)
    }
    @Test
    fun testCreateUserFail() = runBlocking {
        // Use a fixed email and password for the test
        val userEmail = "testUser@gmail.com"
        val userPassword = "password12345"

        // First, create the user to ensure the email is already registered
        suspendCoroutine<Unit> { continuation ->
            Firebase.auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener {
                    Log.d("createUser", "testAuth: user creation succeeded")
                    continuation.resume(Unit)
                }
                .addOnFailureListener { exception ->
                    Log.e("createUser", "testAuth: user creation failed unexpectedly", exception)
                    continuation.resume(Unit)
                }
        }

        // Try to create the same user again to simulate failure
        val result = suspendCoroutine<Boolean> { continuation ->
            Log.d("createUser", "testAuth: creating user with already existing email: $userEmail")

            Firebase.auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener {
                    Log.e("createUser", "testAuth: user creation succeeded unexpectedly")
                    continuation.resume(false) // User creation should fail, but succeeded
                }
                .addOnFailureListener { exception ->
                    Log.d("createUser", "testAuth: user creation failed as expected", exception)
                    continuation.resume(true) // User creation failed as expected
                }
        }

        // Assert that user creation fails as expected
        assertTrue("User creation should fail with an already existing email", result)
    }




    // ADD DOWNLOADIMAGEFROMURL method in the DrawingViewModel
    suspend fun uploadData(newBitmap : ByteArray): Boolean {
        val storageReference =  com.google.firebase.Firebase.storage.reference.child("${com.google.firebase.Firebase.auth.currentUser}/${System.currentTimeMillis()}.png")
        return suspendCoroutine { continuation ->
            val uploadTask = storageReference.putBytes(newBitmap)
            uploadTask
                .addOnFailureListener { e ->
                    Log.e("PICUPLOAD", "Failed !$e")
                    continuation.resume(false)
                }
                .addOnSuccessListener {
                    Log.d("PICUPLOAD", "success")
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        // The download URL is available in uri
                        val downloadUrl = uri.toString()
                        Log.d("DownloadURL", "Download URL: $downloadUrl")
                    }.addOnFailureListener { exception ->
                        // Handle any errors here
                        Log.e("DownloadURL", "Failed to get download URL", exception)
                    }
                    continuation.resume(true)
                }
        }
    }

    fun generateBitmap(): ByteArray {
        //draw a simple picture using a bitmap + canvas
        val bitmap =
            Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888, true)
        val canvas = android.graphics.Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.RED
        canvas.drawCircle(
            Random.nextFloat() * bitmap.width,
            Random.nextFloat() * bitmap.height,
            100f,
            paint
        )
        paint.color = Color.BLUE
        canvas.drawCircle(
            Random.nextFloat() * bitmap.width,
            Random.nextFloat() * bitmap.height,
            150f,
            paint
        )
        val baos = ByteArrayOutputStream()
        //save it into PNG format (in memory, not a file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos)
        return baos.toByteArray() //bytes of the PNG
    }
}