package dk.eleknet.tattoodemo.models

import com.squareup.moshi.JsonClass
import org.joda.time.DateTime

@JsonClass(generateAdapter = true)
data class Post(
    var id: Int = 0,
    var image: PostImage = PostImage(),
    var updatedAt: DateTime = DateTime(),
    var description: String = "",
    var counts: PostCount? = PostCount(),
    var artist: Artist? = Artist(),
    var shop: Shop? = Shop()
)