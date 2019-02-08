package com.desrumaux.androidtoolbox.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.desrumaux.androidtoolbox.R
import com.desrumaux.androidtoolbox.model.Contact
import kotlinx.android.synthetic.main.single_contact.view.*


class ContactsAdapter(private val contactList: List<Contact>, private val mContext: Context) :
    RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.single_contact, null)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Contact) = with(itemView) {
            contact_name.text = context.getString(R.string.info_contact_default_text, item.contactName)
        }
    }
}