package com.example.restrequets.fragments.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restrequets.R
import com.example.restrequets.db.RealmHelper
import com.example.restrequets.model.Image
import com.squareup.picasso.Picasso


class RecyclerFavouritesAdapter( private val values: ArrayList<Image>): RecyclerView.Adapter<RecyclerFavouritesAdapter.ViewHolder>() {

    override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_favourite_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = values[position]
        Picasso.get().load(image.urlRegular).into(holder.ivImage)
        image.likes?.let { likes -> holder.tvLikes?.text = likes.toString() }
        if (image.description.isNullOrEmpty()){
            holder.tvTitle?.visibility = View.GONE
        } else {
            holder.tvTitle?.text = image.description
            holder.tvTitle?.visibility = View.VISIBLE
        }

        holder.tvOfFavourites?.setOnClickListener {
            RealmHelper().deleteImageFromRealm(image.id)
            values.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, values.size)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView? = null
        var tvLikes: TextView? = null
        var tvTitle: TextView? = null
        var tvOfFavourites: TextView? = null

        init {
            ivImage = itemView.findViewById(R.id.ivImage)
            tvLikes = itemView.findViewById(R.id.tvLikes)
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvOfFavourites = itemView.findViewById(R.id.tvOfFavorites)
        }
    }
}