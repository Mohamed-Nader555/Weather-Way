package com.mohamednader.weatherway.Settings.ViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mohamednader.weatherway.Model.Repo.FakeRepository
import com.mohamednader.weatherway.Model.Repo.RepositoryInterface
import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.mohamednader.weatherway.getOrAwaitValue
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
class SettingsFragmentViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel : SettingsFragmentViewModel
    lateinit var repo : RepositoryInterface


    @Before
    fun setup(){
        repo = FakeRepository()
        viewModel = SettingsFragmentViewModel(repo)
    }




    @Test
    fun setLocationAccessOption_Access_LocationSettetd(){
        //Given -> value of location
        val access = "map"
        //when -> setting location acess
        viewModel.setLocationAccessOption(access)
        //Then -> location state flow has been changed
        val result = viewModel.language.getOrAwaitValue()
        assertThat(result, CoreMatchers.`is`("map"))
    }

    @Test
    fun getNotificationOption_ReturnEnabled(){
        //Given -> No Givens
        //when -> getting notification option
        val result = viewModel.notification.getOrAwaitValue()
        //Then -> location state flow has been changed
        assertThat(result, CoreMatchers.`is`("enabled"))
    }



}

private fun <T> StateFlow<T>.getOrAwaitValue(time: Long = 2): T {
    var data: T? = null
    val latch = CountDownLatch(1)

    val job = runBlocking {
        val flow = this@getOrAwaitValue
        launch {
            flow.collect {
                data = it
                latch.countDown()
            }
        }
    }

    if (!latch.await(time, TimeUnit.SECONDS)) {
        job.cancel()
        throw TimeoutException("Flow value was never emitted.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
