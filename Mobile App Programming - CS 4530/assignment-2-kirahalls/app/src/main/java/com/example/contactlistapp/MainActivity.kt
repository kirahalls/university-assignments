package com.example.contactlistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import com.example.contactlistapp.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity()
{
    val binding: ActivityMainBinding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    val recycler by lazy {binding.recycler}

    //Create the main activity, create a view model, set the recycler view and its layout manager, etc.
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val myViewModel : ContactViewModel by viewModels()

        with(recycler)
        {
            //Set recycler to have Linear Layout Manager, and set the adapter to
            //replace the 'add contact' fragment with the full contact view fragment when a contact is clicked
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ContactListAdapter(listOf())
            {  clickedContact ->
                myViewModel.clickedContact = clickedContact
                supportFragmentManager.commit {
                    replace<FullContactFragment>(R.id.fragmentContainerView)
                }
            }
        }

        //Update the adapter's list when the view model's list updates
        myViewModel.observableList.observe(this)
        {
            (recycler.adapter as ContactListAdapter).updateList(it)
            Log.e("LIST SIZE", "${it.size}")

        }

        //When the back button is clicked, replace the full contact view fragment with the add contact fragment
        myViewModel.onBackBtClick.observe(this) {

                Log.e("BackButton", "Back button was clicked")
                supportFragmentManager.commit {
                    replace<ContactFragment>(R.id.fragmentContainerView)

                }
        }

        setContentView(binding.root)
    }
}
