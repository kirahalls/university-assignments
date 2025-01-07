package com.example.contactlistapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.contactlistapp.databinding.FragmentAddContactBinding

class ContactFragment : Fragment()
{
    private lateinit var binding: FragmentAddContactBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentAddContactBinding.inflate(inflater, container, false)

        val contactViewModel : ContactViewModel by activityViewModels()

        //When the new contact button is clicked, create a new contact add add it to the view model, then clear the entry fields
        binding.newContactButton.setOnClickListener{
            val name = binding.NewNameEntry.text.toString()
            val phone = binding.NewPhoneEntry.text.toString()
            val email = binding.NewEmailEntry.text.toString()
            if(name.isEmpty()){
                return@setOnClickListener
            }
            contactViewModel.newItem(name, phone, email)
            binding.NewNameEntry.text.clear()
            binding.NewPhoneEntry.text.clear()
            binding.NewEmailEntry.text.clear()
        }

        return binding.root
    }
}