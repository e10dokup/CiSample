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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import dev.dokup.cisample.ui.theme.MyApplicationTheme

@Composable
fun LegendScreen(modifier: Modifier = Modifier, viewModel: LegendViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val item by produceState<LegendUiState>(
        initialValue = LegendUiState.Loading,
        key1 = lifecycle,
        key2 = viewModel
    ) {
        lifecycle.repeatOnLifecycle(state = STARTED) {
            viewModel.uiState.collect { value = it }
        }
    }

    val itemString = when (val shadowedItem = item) {
        is LegendUiState.Success -> shadowedItem.legendString
        is LegendUiState.Error -> "Error has occurred. check Logcat"
        is LegendUiState.Loading -> null
    }

    LegendScreen(
        item = itemString ?: "No fetched takada's legends",
        onFetchLegend = viewModel::fetchLegend,
        onSave = viewModel::addLegend,
        modifier = modifier
    )
}

@Composable
internal fun LegendScreen(
    item: String,
    onFetchLegend: () -> Unit,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { onFetchLegend() }
        ) {
            Text("Fetch new Takada's Legend")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.CenterHorizontally),
            text = item
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = { onSave(item) }
        ) {
            Text("Save the Takada's Legend")
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        LegendScreen(
            item = "Any SUPER DUPER Takada's Legend",
            onFetchLegend = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        LegendScreen(
            item = "Any SUPER DUPER Takada's Legend",
            onFetchLegend = {},
            onSave = {}
        )
    }
}
