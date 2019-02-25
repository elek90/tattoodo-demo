package dk.eleknet.tattoodemo.network.responses

import com.squareup.moshi.JsonClass
import dk.eleknet.tattoodemo.models.Meta
import dk.eleknet.tattoodemo.models.Post

@JsonClass(generateAdapter = true)
data class PostListResponse(
    val data: List<Post> = listOf(),
    val meta: Meta = Meta()
)