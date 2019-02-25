package dk.eleknet.tattoodemo.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostImage(
    var url: String = "",
    var width: Int = 0,
    var height: Int = 0
)