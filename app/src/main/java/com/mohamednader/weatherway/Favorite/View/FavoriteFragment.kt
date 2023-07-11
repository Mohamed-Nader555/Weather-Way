package com.mohamednader.weatherway.Favorite.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mohamednader.weatherway.MainHome.FabClickListener
import com.mohamednader.weatherway.R

class FavoriteFragment : Fragment(), FabClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onFabClick() {
        Toast.makeText(requireContext(), "This is From FavoriteFragment", Toast.LENGTH_LONG).show()
    }

}