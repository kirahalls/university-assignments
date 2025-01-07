package com.example.drawingapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.drawingapp.databinding.FragmentEditCanvasBinding
import kotlinx.coroutines.launch

/**
 * Simple Fragment class for the editing canvas screen. This fragment uses the view based fragment
 * that has layout specified in fragment_edit_canvas.xml. It also has a Composable button to allow
 * the user to navigate back to the drawing selection screen.
 *
 */
class EditCanvasFragment : Fragment() {

    private lateinit var binding: FragmentEditCanvasBinding
    val viewModel: DrawingViewModel by activityViewModels {
        DrawingViewModelFactory((requireActivity().application as DrawApplication).drawRepository)
    }


    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LayoutDebug", "Orientation: ${resources.configuration.orientation}")
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("LayoutDebug", "Loading landscape layout")
        } else {
            Log.d("LayoutDebug", "Loading portrait layout")
        }

        binding = FragmentEditCanvasBinding.inflate(inflater, container, false)
        binding.TopNavBar.setContent {
            val showUrlPopup by viewModel.showPopup.collectAsState()
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    modifier = Modifier
                        .size(100.dp, 50.dp)

                ) {
                    viewModel.updateBitmap(
                        Bitmap.createBitmap(
                            Resources.getSystem().displayMetrics.widthPixels,
                            Resources.getSystem().displayMetrics.widthPixels,
                            Bitmap.Config.ARGB_8888
                        )
                    )
                    findNavController().navigate(R.id.action_back_button_selected)
                }

                ShareButton(
                    modifier = Modifier
                        .size(100.dp, 50.dp),
                    onClick =  {
                        Log.e("upload", "in on click")
                        lifecycleScope.launch {
                            try {
                                viewModel.uploadData()
                                viewModel.showDialog()
                                Log.e("upload", "in lifecycle scope launch")
                                Log.e("Popup state: " , viewModel.getPopup().toString())

                            } catch (e: Exception) {
                                Log.e("Upload", "Error during image upload: ${e.message}")
                            }
                        }


                    })
                if (showUrlPopup) {
                    CopyablePopup(
                        viewModel.getDownloadUrl(),
                        onDismiss = { viewModel.dismissDialog() })
                }



            }


        }
        binding.BottomBar?.setContent {

            Log.e("bottom bar", "in bottom bar")
            Text("here")
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CopyablePopup(
                    viewModel.getDownloadUrl(),
                    onDismiss = { viewModel.dismissDialog() })
            }
        }

        return binding.root
    }

    @Composable
    fun BackButton(
        modifier: Modifier,
        onClick: () -> Unit
    ) {
        OutlinedButton(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("<- Back")
        }
    }

    @Composable
    fun ShareButton(
        modifier: Modifier,
        onClick: () -> Unit
    ) {
        OutlinedButton(
//            onClick = {
//                Log.e("composable share", "in on click")
//                lifecycleScope.launch {
//                    try {
//                        viewModel.uploadData()
//                        viewModel.showDialog()
//                        Log.e("show popup", "show popup is true")
//                    } catch (e: Exception) {
//                        Log.e("Upload", "Error during image download: ${e.message}")
//                    }
//                }
//            },
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Share Drawing")
        }
    }

//    This composable created with the help of chatGPT to allow users to copy the downloaded image url
    @Composable
    fun CopyablePopup( valueToCopy: String, onDismiss: () -> Unit) {
        val clipboardManager: androidx.compose.ui.platform.ClipboardManager = LocalClipboardManager.current
        val context: Context = LocalContext.current
        Log.e("copyable popup", "in copyable popup")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.copy_popup, null)
        builder
            .setView(dialogView)
            .setPositiveButton("Copy", DialogInterface.OnClickListener { dialog, id ->
                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(valueToCopy))
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                onDismiss()
            })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
        val dialog = builder.create()
        dialog.show()
    }
}

