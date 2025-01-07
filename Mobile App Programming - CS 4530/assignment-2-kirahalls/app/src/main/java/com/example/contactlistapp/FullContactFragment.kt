package com.example.contactlistapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.contactlistapp.databinding.FragmentFullContactBinding

class FullContactFragment : Fragment()
{
    private lateinit var binding: FragmentFullContactBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        binding = FragmentFullContactBinding.inflate(inflater, container, false)

        val contactViewModel : ContactViewModel by activityViewModels()

        //We are guaranteed to have the clickedContact val be not null, so set all the full contact fields
        //with the contact that was clicked to 'trigger' this fragment
        binding.FullNameValue.setText(contactViewModel.clickedContact.name)
        binding.PhoneValue.setText(contactViewModel.clickedContact.phone)
        binding.EmailValue.setText(contactViewModel.clickedContact.email)

        binding.deleteContactBt.setOnClickListener{
            //Call the contact removal function from the viewModel
            contactViewModel.removeItem(contactViewModel.clickedContact)

            //Clear the values of each section once this contact is deleted
            binding.FullNameValue.text.clear()
            binding.PhoneValue.text.clear()
            binding.EmailValue.text.clear()
        }

        binding.backBt.setOnClickListener{
            //Call the back button click method when it is clicked
            contactViewModel.onBackBtClick(true)
        }


        return binding.root
    }
}