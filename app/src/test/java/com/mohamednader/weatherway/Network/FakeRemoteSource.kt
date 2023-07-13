package com.mohamednader.weatherway.Network

import com.mohamednader.weatherway.Model.Place
import com.mohamednader.weatherway.Model.Pojo.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRemoteSource() :RemoteSource {
    override suspend fun getWeatherDataNetwork(params: MutableMap<String, String>): Flow<WeatherResponse> {

        return flowOf(
            WeatherResponse(
                lat = "33.44",
                lon = "-94.04",
                timezone = "America/Chicago",
                timezoneOffset = "-21600",
                current = Current(
                    dt = "1618317040",
                    sunrise = "1618282134",
                    sunset = "1618333901",
                    temp = "284.07",
                    feelsLike = "282.84",
                    pressure = "1019",
                    humidity = "62",
                    dewPoint = "277.08",
                    uvi = "0.89",
                    clouds = "0",
                    visibility = "10000",
                    windSpeed = "6",
                    windDeg = "300",
                    weather = listOf(
                        Weather(
                            id = "500",
                            main = "Rain",
                            description = "light rain",
                            icon = "10d"
                        )
                    )
                ),
                hourly = listOf(
                    Hourly(
                        dt = "1618315200",
                        temp = "282.58",
                        feelsLike = "280.4",
                        pressure = "1019",
                        humidity = "68",
                        dewPoint = "276.98",
                        uvi = "1.4",
                        clouds = "19",
                        visibility = "306",
                        windSpeed = "4.12",
                        windDeg = "296",
                        windGust = "7.33",
                        weather = listOf(
                            Weather(
                                id = "801",
                                main = "Clouds",
                                description = "few clouds",
                                icon = "02d"
                            )
                        ),
                        pop = "0"
                    )
                ),
                daily = listOf(
                    Daily(
                        dt = "1618308000",
                        sunrise = "1618282134",
                        sunset = "1618333901",
                        moonrise = "1618284960",
                        moonset = "1618339740",
                        moonPhase = "0.04",
                        temp = Temp(
                            day = "279.79",
                            min = "275.09",
                            max = "284.07",
                            night = "275.09",
                            eve = "279.21",
                            morn = "278.49"
                        ),
                        feelsLike = FeelsLike(
                            day = "277.59",
                            night = "276.27",
                            eve = "276.49",
                            morn = "276.27"
                        ),
                        pressure = "1020",
                        humidity = "81",
                        dewPoint = "276.77",
                        windSpeed = "3.06",
                        windDeg = "294",
                        windGust = "null",
                        weather = listOf(
                            Weather(
                                id = "500",
                                main = "Rain",
                                description = "light rain",
                                icon = "10d"
                            )
                        ),
                        clouds = "56",
                        pop = "0.2",
                        uvi = "1.93"
                    )
                ),
                alerts = null
            )
        )


    }
}