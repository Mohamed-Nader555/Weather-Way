package com.mohamednader.weatherway.Maps

interface MapResult {
    fun onMapResult(latitude: Double, longitude: Double, address: String)
}