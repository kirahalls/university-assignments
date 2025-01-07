package com.example.drawingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.drawingapp.databinding.FragmentDrawingCanvasBinding


/**
 * A fragment class for the Drawing Canvas. The canvas holds a custom view that
 * lets the user draw on it and customize their brush shape, size, and color.
 */
class DrawingCanvas : Fragment() {

    // The viewModel and binding variables
    val viewModel: DrawingViewModel by activityViewModels {
        DrawingViewModelFactory((requireActivity().application as DrawApplication).drawRepository)
    }

    lateinit var binding: FragmentDrawingCanvasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDrawingCanvasBinding.inflate(inflater)

        // Observe the bitmap for changes and set the bitmap of the custom view
        viewModel.observableBitmap.observe(viewLifecycleOwner) { bitmap ->
            binding.customView.setBitmap(bitmap) // Update the CustomView when bitmap changes
        }

        // Observe when the shape changes and set the brush shape of the custom View
        viewModel.observableShape.observe(viewLifecycleOwner) {
            binding.customView.setBrushShape(viewModel.observableShape.value!!)
        }

        // Observe when the brush color changes and set the color of the custom view
        viewModel.observableColor.observe(viewLifecycleOwner) {
            binding.customView.setBrushColor(viewModel.observableColor.value!!)
        }

        // Observe when the brush size changes and update it in the custom view
        viewModel.observableSize.observe(viewLifecycleOwner) {
            binding.customView.setBrushSize(viewModel.observableSize.value!!)
        }

        return binding.root
    }

    // Update the bitmap in the customView/ViewModel to keep consistency across lifecycle changes
    override fun onStop() {
        super.onStop()
        binding.customView.updateBitmap(viewModel)
    }

    // Update the bitmap in the customView/ViewModel to keep consistency across lifecycle changes
    override fun onPause() {
        super.onPause()
        binding.customView.updateBitmap(viewModel)
    }
}