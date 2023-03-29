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

package dev.dokup.cisample.ui.legend

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.dokup.cisample.data.LegendRepository
import dev.dokup.cisample.data.remote.api.misc.Future
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LegendViewModel @Inject constructor(
    private val legendRepository: LegendRepository
) : ViewModel() {

    val uiState: MutableStateFlow<LegendUiState> = MutableStateFlow(LegendUiState.Loading)

    fun fetchLegend() {
        viewModelScope.launch {
            legendRepository.fetchLegend.collect { result ->
                when (result) {
                    is Future.Proceeding -> {
                        uiState.value = LegendUiState.Loading
                    }
                    is Future.Success -> {
                        uiState.value = LegendUiState.Success(
                            result.value.text
                        )
                    }
                    is Future.Error -> {
                        Log.e(
                            "LegendViewModel",
                            "Error on LegendViewModel::fetchLegend",
                            result.error
                        )
                        uiState.value = LegendUiState.Error(
                            result.error
                        )
                    }
                }
            }
        }
    }

    fun addLegend(name: String) {
        viewModelScope.launch {
            legendRepository.add(name)
        }
    }
}

sealed interface LegendUiState {
    object Loading : LegendUiState
    data class Error(val throwable: Throwable) : LegendUiState
    data class Success(val legendString: String) : LegendUiState
}
