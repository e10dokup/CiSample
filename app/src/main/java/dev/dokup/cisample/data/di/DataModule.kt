/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.dokup.cisample.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import dev.dokup.cisample.data.LegendRepository
import dev.dokup.cisample.data.DefaultLegendRepository
import dev.dokup.cisample.data.remote.api.TakadaLegendResponse
import dev.dokup.cisample.data.remote.api.misc.Future
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsLegendRepository(
        legendRepository: DefaultLegendRepository
    ): LegendRepository
}

class FakeLegendRepository @Inject constructor() : LegendRepository {
    override val fetchLegend: Flow<Future<TakadaLegendResponse>> = flowOf(fakeFetchedLegend)
    override val legends: Flow<List<String>> = flowOf(fakeLegends)

    override suspend fun add(name: String) {
        throw NotImplementedError()
    }
}

val fakeFetchedLegend = Future.Success(TakadaLegendResponse(no = 1, text = "sample takada legend"))
val fakeLegends = listOf("One", "Two", "Three")
