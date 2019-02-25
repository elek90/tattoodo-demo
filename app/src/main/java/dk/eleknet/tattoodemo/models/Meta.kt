package dk.eleknet.tattoodemo.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    var pagination: Pagination = Pagination()
)