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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

/**
 * Unit tests for [DefaultLegendRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultLegendRepositoryTest {

    @Test
    fun legends_fetchLegend_successGetLegend() = runTest {
        val api = FakeLegendApi()
        val repository = DefaultLegendRepository(
            takadaLegendsApi = api,
            legendDao = FakeLegendDao()
        )

        val features = mutableListOf<Future<TakadaLegendResponse>>()
        repository.fetchLegend.toCollection(features)

        assertEquals(3, features.size)
        assertEquals(true, features[0] is Future.Proceeding)
        assertEquals(true, features[1] is Future.Success)

        val succeededResult = features[1] as Future.Success
        val expectedNo = 15
        val expectedText = """
                ある意地の悪い華族が高田健志を自宅に招き、「1時間私を楽しませてみせよ」と大きな砂時計を逆さにした。
                もちろん平民の話を最後まで聞くつもりなど彼にはなく、頃合いを見て追い返してやるつもりであった。
                そろそろ5分経ったと見た彼は「つまらん」と立ち上がった。砂はすっかり落ち切っていた。
            """.trimIndent()
        assertEquals(expectedNo, succeededResult.value.no)
        assertEquals(expectedText, succeededResult.value.text)
    }

}

private class FakeLegendDao : LegendDao {

    private val data = mutableListOf<Legend>()

    override fun getLegends(): Flow<List<Legend>> = flow {
        emit(data)
    }

    override suspend fun insertLegend(item: Legend) {
        data.add(0, item)
    }
}

private class FakeLegendApi : TakadaLegendsApi {

    private val response = TakadaLegendResponse(
        no = 15,
        text = """
            ある意地の悪い華族が高田健志を自宅に招き、「1時間私を楽しませてみせよ」と大きな砂時計を逆さにした。
            もちろん平民の話を最後まで聞くつもりなど彼にはなく、頃合いを見て追い返してやるつもりであった。
            そろそろ5分経ったと見た彼は「つまらん」と立ち上がった。砂はすっかり落ち切っていた。
        """.trimIndent()
    )
    override suspend fun getRandomLegend(): Response<TakadaLegendResponse> {
        return Response.success(response)
    }
}
