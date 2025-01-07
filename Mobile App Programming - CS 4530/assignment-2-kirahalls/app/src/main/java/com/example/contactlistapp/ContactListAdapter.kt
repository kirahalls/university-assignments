package com.example.contactlistapp

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlistapp.databinding.ContactBinding

//Adapter for the recycler view. Provide a list of contact items and an onClick val
class ContactListAdapter(private var contactItems: List<ContactItem>,
                         private val onClick : (item: ContactItem) -> Unit
) : RecyclerView.Adapter<ContactListAdapter.ViewHolder>()
{
    //Update the list by updating the adapater's knowledge of the contact list and notify that the data set changed
    fun updateList(newList: List<ContactItem>)
    {
        Log.e("UPDATE", "item list updated")
        contactItems = newList
        notifyDataSetChanged()
    }

    //ViewHolder for the recycler view, uses the contact.xml layout and binding for each view
    inner class ViewHolder(val binding: ContactBinding): RecyclerView.ViewHolder(binding.root)

    //Initial creation of view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        Log.e("VIEWHOLDER", "View holder was created")
        return ViewHolder(ContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //Returns the number of contacts in the list
    override fun getItemCount(): Int = contactItems.size

    //Data binding - Set the view's name text field and set the onClickListener for when a user clicks on the contact
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        Log.e("VIEWHOLDER", "bound viewholder")
        val item = contactItems[position]
        holder.binding.smallNameValue.text = item.name
        holder.binding.root.setOnClickListener{
            onClick(item)
        }

    }




}

