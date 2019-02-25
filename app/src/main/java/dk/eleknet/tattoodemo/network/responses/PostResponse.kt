package dk.eleknet.tattoodemo.network.responses

import com.squareup.moshi.JsonClass
import dk.eleknet.tattoodemo.models.Meta
import dk.eleknet.tattoodemo.models.Post

@JsonClass(generateAdapter = true)
data class PostResponse(
    val data: Post = Post()
)