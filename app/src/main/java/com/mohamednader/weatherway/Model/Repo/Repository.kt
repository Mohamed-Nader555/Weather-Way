package com.mohamednader.weatherway.Model.Repo

import com.mohamednader.weatherway.Model.Pojo.WeatherResponse
import com.mohamednader.weatherway.Network.RemoteSource
import kotlinx.coroutines.flow.Flow

class Repository private constructor(remoteSource: RemoteSource) : RepositoryInterface {

    private val TAG = "Repository_INFO_TAG"


    private val remoteSource: RemoteSource
    //private val localSource: LocalSource

    init {
        this.remoteSource = remoteSource
      //  this.localSource = localSource
    }

    companion object {
        private var repo: Repository? = null
        fun getInstance(
            remoteSource: RemoteSource,
        //    localSource: LocalSource
        ): Repository {
            return repo ?: synchronized(this) {
                val instance = Repository(remoteSource/*, localSource*/)
                repo = instance
                instance
            }
        }
    }




    override suspend fun getWeatherDataNetwork(params: MutableMap<String, String>): Flow<WeatherResponse> {
        return remoteSource.getWeatherDataNetwork(params)
    }
}