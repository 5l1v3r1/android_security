package com.desrumaux.androidtoolbox.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.desrumaux.androidtoolbox.R
import com.desrumaux.androidtoolbox.model.api.Result
import com.desrumaux.androidtoolbox.transform.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.single_user.view.*

class UserAdapter(private val users: List<Result>, private val mContext: Context) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.single_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Result) = with(itemView) {
            name.text = item.name.toString()
            email.text = item.email.toString()
            address.text = item.location.toString()
            Picasso.get().load(item.picture?.medium.toString()).transform(RoundedTransformation(35,0)).resize(70,70).into(avatar)
        }
    }
}
