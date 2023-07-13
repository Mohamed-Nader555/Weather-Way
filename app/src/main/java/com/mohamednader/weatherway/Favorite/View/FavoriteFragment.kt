package com.mohamednader.weatherway.Favorite.View

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamednader.weatherway.Database.ConcreteLocalSource
import com.mohamednader.weatherway.Favorite.FavoriteResult.View.FavoriteResultActivity
import com.mohamednader.weatherway.Favorite.ViewModel.FavoriteFragmentViewModel
import com.mohamednader.weatherway.Favorite.ViewModel.FavoriteFragmentViewModelFactory
import com.mohamednader.weatherway.MainHome.View.FabClickListener
import com.mohamednader.weatherway.Maps.MapResult
import com.mohamednader.weatherway.Maps.MapsActivity
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Repo.Repository
import com.mohamednader.weatherway.Network.ApiClient
import com.mohamednader.weatherway.SharedPreferences.ConcreteSharedPrefsSource
import com.mohamednader.weatherway.Utilities.Constants
import com.mohamednader.weatherway.Utilities.MapResultListenerHolder
import com.mohamednader.weatherway.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment(), FabClickListener, MapResult, OnFavClickListener {

    private val TAG = "FavoriteFragment_INFO_TAG"

    private lateinit var binding: FragmentFavoriteBinding

    //View Model Members
    private lateinit var favoriteViewModel: FavoriteFragmentViewModel
    private lateinit var favoriteFactory: FavoriteFragmentViewModelFactory

    //Needed Variables
    val LOCATION_PICK: Int = 11
    lateinit var favPlacesAdapter: FavoritePlacesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModel
        favoriteFactory = FavoriteFragmentViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
                ConcreteLocalSource(requireContext()),
                ConcreteSharedPrefsSource(requireContext())
            )
        )
        favoriteViewModel =
            ViewModelProvider(this, favoriteFactory).get(FavoriteFragmentViewModel::class.java)

        recyclerViewConfig()

        lifecycleScope.launchWhenStarted {
            favoriteViewModel.placesList.collect { places ->
                Log.i(TAG, "onCreate: ${places.size}")
                if (places.isNotEmpty()) {
                    showViews()
                    favPlacesAdapter.submitList(places)
                } else {
                    hideViews()
                }
            }
        }
    }

    private fun recyclerViewConfig() {
        binding.favRecyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.favRecyclerView.layoutManager = linearLayoutManager
        favPlacesAdapter = FavoritePlacesAdapter(requireContext(), this)
        binding.favRecyclerView.adapter = favPlacesAdapter
    }

    private fun checkPermissions(): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return result
    }

    override fun onFabClick() {
        Toast.makeText(requireContext(), "This is From FavoriteFragment", Toast.LENGTH_LONG).show()
        if (checkPermissions()) {
            MapResultListenerHolder.listener = this as MapResult
            val resMapView = Intent(requireContext(), MapsActivity::class.java)
            resMapView.putExtra(Constants.sourceFragment,Constants.favoriteFragment)
            startActivityForResult(
                resMapView, LOCATION_PICK
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Log.i(TAG, "onActivityResult: FROM FAVORITE FRAGMENT $data")
//        if (requestCode == 1100 && resultCode == Activity.RESULT_OK) {
//            val place: Place = Autocomplete.getPlaceFromIntent(data)
//            searchBool = true
//            searchLat = place.getLatLng().latitude
//            searchLong = place.getLatLng().longitude
//            searchAddress = place.getName()
//            setDownloadAddress(searchAddress)
//            requestOutput(searchLat, searchLong, searchAddress)
//        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//            val status: Status = Autocomplete.getStatusFromIntent(data)
//            Log.e("Failed", "onActivityResult: When Search " + status.statusMessage)
//        }
    }

    override fun onMapResult(
        latitude: Double, longitude: Double, address: String, sourceFragment: String
    ) {
        if (sourceFragment == Constants.favoriteFragment) {
            val place = Place(0, latitude.toString(), longitude.toString(), address)
            if (latitude != null && longitude != null) {
                favoriteViewModel.addPlaceToFav(place)
                Toast.makeText(
                    requireContext(), place.address + " Added From Fav!", Toast.LENGTH_SHORT
                ).show()
            }
            Log.i(TAG, "onFinishDialog: IN FAVORITE ACTIVITY  : $latitude, $longitude, $address ")

        }
    }

    override fun onClickDeletePlace(place: Place) {
        AlertDialog.Builder(requireContext()).setMessage("Do you want to delete this place")
            .setCancelable(false).setPositiveButton("Yes, Delete it") { dialog, _ ->
                favoriteViewModel.deletePlaceFromFav(place)
                Toast.makeText(
                    requireContext(), place.address + " Removed From Fav!", Toast.LENGTH_SHORT
                ).show()
            }.setNegativeButton("No, Keep it", null).show()
    }

    override fun onClickPlace(place: Place) {
        val intent = Intent(requireContext(), FavoriteResultActivity::class.java)
        intent.putExtra("place_data", place)
        startActivity(intent)


        Toast.makeText(requireContext(), "You Cliked ${place.address}", Toast.LENGTH_SHORT).show()
    }


    private fun showViews() {

        binding.noItemsTv.visibility = View.GONE
        binding.favRecyclerView.visibility = View.VISIBLE
    }


    private fun hideViews() {
        binding.noItemsTv.visibility = View.VISIBLE
        binding.favRecyclerView.visibility = View.GONE
    }


}