package com.mohamednader.weatherway.Model.Repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mohamednader.weatherway.Database.FakeLocalSource
import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Network.FakeRemoteSource
import com.mohamednader.weatherway.SharedPreferences.FakeSharedPrefsSource
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.junit.Assert
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

class RepositoryTest {

    val place1 = Place(1, "lat1", "lat1", "add1")
    val place2 = Place(2, "lat2", "lat2", "add2")
    val place3 = Place(3, "lat3", "lat3", "add3")
    val place4 = Place(4, "lat4", "lat4", "add4")

    lateinit var fakeLocalSource: FakeLocalSource
    lateinit var fakeRemoteSorce: FakeRemoteSource
    lateinit var fakeSharedPrefs: FakeSharedPrefsSource
    lateinit var repo: Repository

    val localList: MutableList<Place> = mutableListOf(place1, place2 , place3 , place4)
    val sharedPrefs: MutableMap<String, String> = mutableMapOf()

    @Before
    fun setup() {
        fakeLocalSource = FakeLocalSource(localList)
        fakeRemoteSorce = FakeRemoteSource()
        fakeSharedPrefs = FakeSharedPrefsSource(sharedPrefs)
        repo = Repository(fakeRemoteSorce, fakeLocalSource, fakeSharedPrefs)
    }

    @Test
    fun insertPlace_place_numOfPlacesNotEqual() = runBlockingTest{
        //Given -> Place Object
        val place = Place(20 , "lat20" , "lon20","add20" )
        //When Insert Place and get places
        repo.insertPlace(place)
        val result = repo.getPlacesFromDatabase()
        ///Then the orignal list doesnt equal the result list
        assertNotEquals(result.toList().size, localList.toList().size)
    }

    @Test
    fun deletePlace_place_numOfPlacesNotEqual() = runBlockingTest{
        //Given -> Place Object
        val place = Place(2, "lat2", "lat2", "add2")
        //When Insert Place and get places
        repo.deletePlace(place)
        val result = repo.getPlacesFromDatabase()
        ///Then the orignal list doesnt equal the result list
        assertNotEquals(result.toList().size, localList.toList().size)
    }

}