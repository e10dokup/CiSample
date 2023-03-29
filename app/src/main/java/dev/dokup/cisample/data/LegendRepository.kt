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

package dev.dokup.cisample.data

import dev.dokup.cisample.data.local.database.Legend
import dev.dokup.cisample.data.local.database.LegendDao
import dev.dokup.cisample.data.remote.api.TakadaLegendResponse
import dev.dokup.cisample.data.remote.api.TakadaLegendsApi
import dev.dokup.cisample.data.remote.api.misc.Future
import dev.dokup.cisample.data.remote.api.misc.apiFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface LegendRepository {
    val fetchLegend: Flow<Future<TakadaLegendResponse>>
    val legends: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultLegendRepository @Inject constructor(
    private val takadaLegendsApi: TakadaLegendsApi,
    private val legendDao: LegendDao
) : LegendRepository {

    override val fetchLegend: Flow<Future<TakadaLegendResponse>> = apiFlow {
        takadaLegendsApi.getRandomLegend()
    }

    override val legends: Flow<List<String>> =
        legendDao.getLegends().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        legendDao.insertLegend(Legend(name = name))
    }
}
