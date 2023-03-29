package dev.dokup.cisample.data.remote.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.dokup.cisample.data.remote.api.TakadaLegendsApi
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    companion object {
        private const val API_BASE_URL = "https://tools.ic731.net"
    }

    @Provides
    fun provideTakadaLegendApi(retrofit: Retrofit): TakadaLegendsApi {
        return retrofit.create(TakadaLegendsApi::class.java)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }
}
