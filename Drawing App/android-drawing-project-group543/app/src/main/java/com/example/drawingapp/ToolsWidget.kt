package com.example.drawingapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.drawingapp.databinding.FragmentToolsWidgetBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


/**
 * A fragment for the tools widget. Allows user to select a brush size, color, and
 * shape. Stores each of these things in a viewModel and updates the view as necessary
 */
class ToolsWidget : Fragment() {

    private lateinit var binding: FragmentToolsWidgetBinding

    val viewModel: DrawingViewModel by activityViewModels {
        DrawingViewModelFactory((requireActivity().application as DrawApplication).drawRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolsWidgetBinding.inflate(inflater, container, false)


        binding.currentColorButton.setBackgroundColor(viewModel.observableColor.value!!)

        binding.currentColorButton.setOnClickListener {
            createColorDialog()
        }

        binding.sizeButton.setOnClickListener {
            createSizeDialog()
        }

        binding.brushShapeButton.setOnClickListener {
            createShapeDialog()
        }

        binding.saveButton.setOnClickListener()
        {
            createSaveDialog()
        }

        return binding.root
    }

    // Code created with help of ChatGPT and through the example on the GitHub Repository for this color selector library
    //When the user clicks on the "color" button, the color selector dialog pops up
    fun createColorDialog() {
        ColorPickerDialog.Builder(requireContext())
            .setTitle("ColorPicker")
            .setPreferenceName("MyColorPickerDialog")
            //When the user clicks "confirm", set the new color
            .setPositiveButton(getString(R.string.confirm),
                object : ColorEnvelopeListener {
                    override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                        setLayoutColor(envelope)
                        viewModel.newColor(envelope.color)
                    }
                })
            //When the user clicks "cancel", dismiss the dialog without doing anything
            .setNegativeButton(getString(R.string.cancel),
                DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.dismiss()
                })
            .attachAlphaSlideBar(true)
            .attachBrightnessSlideBar(true)
            .setBottomSpace(12)
            .show()
    }

    //Set the view Model's current color to the newly selected color, and changes the
    // color of the 'current color button' to the new color
    private fun setLayoutColor(envelope: ColorEnvelope) {
        val color = envelope.color
        Log.e("COLOR CHANGED", "Current Color: ${color}")
        binding.currentColorButton.setBackgroundColor(color)
        viewModel.newColor(color)
    }

    // When the user selects the brush size button, the brush size selector dialog pops up
    // and lets them choose a new brush size. Allows users to confirm their selection
    // or cancel to keep their previous b rush size
    fun createSizeDialog() {
        Log.e("FUNCTION", "In Size dialog function")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.brush_size_popup, null)
        builder
            .setView(dialogView)
            .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialog, id ->
                val brushSize = dialogView.findViewById<SeekBar>(R.id.sizeBar).progress
                viewModel.newBrushSize(brushSize)
            })
            .setNegativeButton(R.string.cancel,
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
        val dialog = builder.create()
        dialog.show()
    }

    // When the user selects the "brush shape" button, the shape selector dialog pops up
    // and allows the user to select either a rectangle or circle brush shape. Stores this new value
    // in the view model and allows the user to select the 'confirm' button just for clarity
    fun createShapeDialog() {
        Log.e("FUNCTION", "In Shape dialog function")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.shape_popup, null)
        val circleButton = dialogView.findViewById<ImageButton>(R.id.circleButton)
        val rectangleButton = dialogView.findViewById<ImageButton>(R.id.rectangleButton)

        circleButton.setOnClickListener {
            viewModel.newBrushShape("Circle")
        }

        rectangleButton.setOnClickListener {
            viewModel.newBrushShape("Rectangle")
        }

        builder
            .setView(dialogView)
            .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialog, id ->

            })
        val dialog = builder.create()
        dialog.show()
    }

    // When the user selects the "Save Image" button, the save dialog pops up
    // and allows the user to enter a name. If it isn't empty send the trimmed name
    // and current bitmap to the viewModel
    fun createSaveDialog() {
        Log.e("FUNCTION", "In Save dialog function")

        // Inflate the custom view with an EditText field for entering the name
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.set_name, null)
        val editText = dialogView.findViewById<EditText>(R.id.editTextDrawingName)

        builder.setView(dialogView)
            .setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialog, id ->
                // Get the name entered by the user
                val name = editText.text.toString().trim()

                if (name.isNotEmpty()) {
                    // Save the drawing with the provided name
                    val currentBitmap = viewModel.observableBitmap.value
                    if (currentBitmap != null) {
                        viewModel.saveImage(name)
                        Log.e("Save", "Drawing saved as: $name")
                    } else {
                        Log.e("ViewModel", "No bitmap to save!")
                    }
                }
            })
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }
}
