package com.du4r.instagramclone.search.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.du4r.instagramclone.R
import com.du4r.instagramclone.common.model.UserAuth

class SearchAdapter(private val itemClick: (String) -> Unit) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    var users: List<UserAuth> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: UserAuth){
            itemView.findViewById<ImageView>(R.id.search_img_user).setImageURI(user.photoUri)
            itemView.findViewById<TextView>(R.id.search_txt_username).text = user.name
            itemView.setOnClickListener {
                itemClick.invoke(user.UUID)
            }
        }
    }
}