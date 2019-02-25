package dk.eleknet.tattoodemo.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Pagination(
    var total: Int = 0,
    var count: Int = 0,
    var per_page: Int = 0,
    var current_page: Int = 0,
    var total_pages: Int = 0,
    var links: PaginationLinks = PaginationLinks()
)