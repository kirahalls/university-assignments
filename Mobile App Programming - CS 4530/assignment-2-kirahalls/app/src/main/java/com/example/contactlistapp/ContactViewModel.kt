package com.example.contactlistapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//Set up contact class with vals for name, phone, and email
data class ContactItem(val name: String, val phone: String, val email: String)

//ContactViewModel class
class ContactViewModel : ViewModel()
{
    //Set up variables to hold the data of the contact item that was clicked to pass to full contact fragment
    lateinit var clickedContact : ContactItem

    //Model
    private val actualList = MutableLiveData(mutableListOf<ContactItem>())

    //The list we can track
    val observableList = actualList as LiveData<out List<ContactItem>>

    //Info for registering when the back button is clicked in Full Contact Fragment
    private val backButtonClick: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val onBackBtClick = backButtonClick as LiveData<Boolean>

    //When a new contact is added, update the actualList and force the observers to fire
    fun newItem(name: String, phone: String, email: String)
    {
        Log.e("ITEM", "new item added")
        actualList.value!!.add(ContactItem(name, phone, email))
        actualList.value = actualList.value
    }

    //When a contact is removed, update the actualList and force the observers to fire
    fun removeItem(item: ContactItem)
    {
        actualList.value!!.remove(item)
        actualList.value = actualList.value
    }

    //Set the backButtonClick value so the MainActivity/Adapter can see it
    fun onBackBtClick(v : Boolean)
    {
        backButtonClick.value = v
    }
}