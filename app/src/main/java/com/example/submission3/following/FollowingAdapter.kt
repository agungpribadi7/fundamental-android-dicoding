package com.example.submission3.following

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission3.R
import com.example.submission3.User

class FollowingAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listUser[position]
        Glide.with(holder.itemView.context)
                .load(data.img)
                .apply(RequestOptions().override(250, 250))
                .into(holder.img)
        holder.username.text = data.username
        holder.name.text = data.name
        holder.company.text = data.company
        holder.location.text = data.location
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name : TextView = itemView.findViewById(R.id.tv_name)
        var username : TextView = itemView.findViewById(R.id.tv_username)
        var img : ImageView = itemView.findViewById(R.id.img_item_photo)
        var company : TextView = itemView.findViewById(R.id.tv_company)
        var location : TextView = itemView.findViewById(R.id.tv_location)
    }
}