package com.mohamednader.weatherway.Model.Repo

import com.mohamednader.weatherway.Database.LocalSource
import com.mohamednader.weatherway.Network.RemoteSource
import com.mohamednader.weatherway.SharedPreferences.SharedPrefsSource

interface RepositoryInterface : RemoteSource, LocalSource, SharedPrefsSource {
}