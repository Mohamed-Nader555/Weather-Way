package com.mohamednader.weatherway.Favorite.View

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.databinding.ItemFavoritePlaceBinding


class FavoritePlacesAdapter(
    private val context: Context, private val listener: OnFavClickListener
) : ListAdapter<Place, FavoritePlacesViewHolder>(FavoritePlacesDiffUtil()) {

    private lateinit var binding: ItemFavoritePlaceBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePlacesViewHolder {
        binding =
            ItemFavoritePlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritePlacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritePlacesViewHolder, position: Int) {

        val place: Place = getItem(position)

        binding.favAddress.text = place.address

        binding.root.setOnClickListener{
            listener.onClickPlace(place)
        }

        binding.deleteImgBtn.setOnClickListener{
            listener.onClickDeletePlace(place)
        }

    }

}

class FavoritePlacesViewHolder(var binding: ItemFavoritePlaceBinding) :
    RecyclerView.ViewHolder(binding.root)

class FavoritePlacesDiffUtil : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }


}