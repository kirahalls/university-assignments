package com.example.drawingapp

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.drawingapp.databinding.FragmentSelectDrawingScreenBinding
import com.example.drawingapp.ui.theme.FirebaseDemoTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


/**
 * Fragment class for the screen that allows users to select a previous drawing to edit. Created
 * the UI using Compose elements. Each drawing the user has 'saved' will be displayed in a lazy
 * column in a clickable image display element. Once the user clicks on a drawing, that bitmap
 * will be automatically drawn to the canvas
 */
class SelectDrawingScreen : Fragment() {

    val viewModel: DrawingViewModel by activityViewModels {
        DrawingViewModelFactory((requireActivity().application as DrawApplication).drawRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectDrawingScreenBinding.inflate(layoutInflater)

        //Added to be hidden from user so if they delete all images it doesn't crash
        val defaultBitmap: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        binding.selectDrawingScreen.setContent {
            //Top App bar
            Scaffold(topBar = {
                CenterAlignedTopAppBar(title = { Text("") }, actions = {
                    // Top bar has two buttons in a row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //login button
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            OutlinedButton(
                                onClick = {
                                    findNavController().navigate(R.id.LoginFragment)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = androidx.compose.ui.graphics.Color.White,
                                    contentColor = androidx.compose.ui.graphics.Color.Black
                                )
                            ) {
                                Text("Login")
                            }
                        }
                        // Create new drawing Button
                        Box(
                            contentAlignment = Alignment.CenterStart

                        ) {
                            OutlinedButton(
                                onClick = {
                                    findNavController().navigate(R.id.action_drawing_selected)
                                }, colors = ButtonDefaults.buttonColors(
                                    containerColor = androidx.compose.ui.graphics.Color.White,
                                    contentColor = androidx.compose.ui.graphics.Color.Black
                                )
                            ) {
                                Text("Create new drawing")
                            }
                        }

                        // Import Button
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            OutlinedButton(
                                onClick = {
                                    createImportDialog()
                                }, colors = ButtonDefaults.buttonColors(
                                    containerColor = androidx.compose.ui.graphics.Color.White,
                                    contentColor = androidx.compose.ui.graphics.Color.Black
                                )
                            ) {
                                Text("Import Drawing")
                            }
                        }
                    }
                })
            }) { innerPadding ->

                // Observe the list of saved drawings from the ViewModel
                val drawings by viewModel.allDrawings.observeAsState(emptyList())
                if (drawings.isNotEmpty()) {
                    // LazyColumn to display all saved drawings
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        items(drawings) { drawing ->
                            // Show each saved drawing as an image, clickable
                            // Check if the drawing is the "hidden" bitmap
                            val isRemovable = drawing.pict != defaultBitmap
                            DrawingDisplay(
                                drawing = drawing.pict,
                                modifier = Modifier.padding(16.dp),
                                onClick = {
                                    // When clicked, update the ViewModel's bitmap to the selected drawing's bitmap
                                    viewModel.updateBitmap(
                                        drawing.pict.copy(
                                            Bitmap.Config.ARGB_8888, true
                                        )
                                    )
                                    // Navigate to the drawing screen where user can edit it
                                    findNavController().navigate(R.id.action_drawing_selected)
                                },
                                onButtonClick = {
                                    // Remove image
                                    viewModel.deleteDrawing(drawing.id)
                                    Log.e("SelectDrawingScreen", "DeleteBitmap")
                                },
                                isRemovable = isRemovable
                            )
                        }
                    }
                } else {
                    // Display the "No drawings available" message when there are no drawings
                    NoneDisplay("No drawings available.")
                }
            }
        }
        return binding.root
    }

    @Composable
    fun DrawingDisplay(
        drawing: Bitmap,
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
        onButtonClick: () -> Unit,
        isRemovable: Boolean = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
        ) {
            // Image
            Image(
                painter = BitmapPainter(drawing.asImageBitmap()),
                contentDescription = "Clickable Drawing",
                modifier = Modifier
                    .size(300.dp) //Change for image size
                    .clickable(onClick = onClick)
            )

            // Button for deleting image from repo
            if (isRemovable) {
                Button(
                    onClick = onButtonClick,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Remove Image")
                }
            }
        }
    }

    @Composable
    fun NoneDisplay(text: String) {
        // Display a message when there are no drawings
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = text)
        }
    }

    fun createImportDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.set_url, null)
        val editText = dialogView.findViewById<EditText>(R.id.editUrl)

        builder.setView(dialogView)
            .setPositiveButton(R.string.confirm) { dialog, id ->
                val url = editText.text.toString().trim()

                if (url.isNotEmpty()) {
                    val name = "Imported Drawing" // TODO do a different name
                    lifecycleScope.launch {
                        try {
                            viewModel.downloadImageFromUrl(url, name)
                        } catch (e: Exception) {
                            Log.e("Import", "Error during image download: ${e.message}")
                        }
                    }
                } else {
                    Log.e("Import", "No URL entered")
                }
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                FirebaseDemoTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        var user by remember { mutableStateOf(Firebase.auth.currentUser) }
                        val auth = FirebaseAuth.getInstance()
                        Column {
                            if (user == null) {
                                Column {
                                    var email by remember { mutableStateOf("") }
                                    var password by remember { mutableStateOf("") }
                                    Text("Not logged in")
                                    OutlinedTextField(
                                        value = email,
                                        onValueChange = { email = it },
                                        label = { Text("Email") }
                                    )
                                    OutlinedTextField(
                                        value = password,
                                        onValueChange = { password = it },
                                        label = { Text("Password") },
                                        visualTransformation = PasswordVisualTransformation()
                                    )
                                    Row {
                                        Button(onClick = {
                                            Firebase.auth.signInWithEmailAndPassword(email, password)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        user = Firebase.auth.currentUser
                                                    } else {
                                                        email = "Login failed, try again"
                                                    }
                                                }
                                        }) {
                                            Text("Log In")
                                        }
                                        Button(onClick = {
                                            requireActivity().onBackPressedDispatcher.onBackPressed()
                                        }) {
                                            Text("Back")
                                        }
                                        Button(onClick = {
                                            Firebase.auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        user = Firebase.auth.currentUser
                                                    } else {
                                                        email = "Create user failed, try again"
                                                        Log.e("Create user error", "${task.exception}")
                                                    }
                                                }
                                        }) {
                                            Text("Sign Up")
                                        }
                                    }
                                }
                            } else {
                                Text("Welcome ${user!!.email} with id: ${user!!.uid}")
                                var dataString by remember { mutableStateOf("") }
                                Button(onClick = {
                                    Firebase.auth.signOut()
                                    user = null
                                }) {
                                    Text("Sign out")
                                }
                                Text("Data string: $dataString")
                                Button(onClick = {
                                    requireActivity().onBackPressedDispatcher.onBackPressed()
                                }) {
                                    Text("Back")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





