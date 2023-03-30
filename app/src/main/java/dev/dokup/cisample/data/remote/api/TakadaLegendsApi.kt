package dev.dokup.cisample.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface TakadaLegendsApi {

    @Headers("User-Agent: CISampleTakadaLegends")
    @GET("api/kenshi/takada.php")
    suspend fun getRandomLegend(): Response<TakadaLegendResponse>
}
