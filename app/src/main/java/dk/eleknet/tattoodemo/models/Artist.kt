package dk.eleknet.tattoodemo.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Artist(
    var id: Int = 0,
    var name: String = "",
    var pins: Int = 0
)