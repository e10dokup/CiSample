package dev.dokup.cisample.data.remote.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TakadaLegendResponse(
    @SerialName("No") val no: Int,
    @SerialName("text") val text: String
)
