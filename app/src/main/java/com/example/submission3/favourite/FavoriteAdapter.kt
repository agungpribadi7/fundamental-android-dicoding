package com.example.submission3.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission3.custom.CustomOnClickListener
import com.example.submission3.R
import com.example.submission3.User
import java.util.ArrayList

class FavoriteAdapter(private val listener : CustomOnClickListener) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    var itemUser = ArrayList<User>()

    fun setListUser(list: ArrayList<User>){
        this.itemUser.clear()
        this.itemUser.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val viewUser : View) : RecyclerView.ViewHolder(viewUser) {
        var name : TextView = viewUser.findViewById(R.id.tv_name)
        var username : TextView = viewUser.findViewById(R.id.tv_username)
        var img : ImageView = viewUser.findViewById(R.id.img_item_photo)
        var company : TextView = viewUser.findViewById(R.id.tv_company)
        var location : TextView = viewUser.findViewById(R.id.tv_location)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = itemUser[position]

        Glide.with(holder.viewUser.context)
            .load(user.img)
            .apply{ RequestOptions().override(55, 55)}
            .into(holder.img)

        holder.name.text = user.name
        holder.username.text = user.username
        holder.company.text = user.company
        holder.location.text = user.location

        holder.viewUser.setOnClickListener{ listener.onClick(itemUser[position]) }
    }

}