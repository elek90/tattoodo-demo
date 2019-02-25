package dk.eleknet.tattoodemo.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostCount(
    var likes: Int = 0,
    var comments: Int = 0,
    var pins: Int = 0
)