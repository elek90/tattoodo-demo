package dk.eleknet.tattoodemo.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Shop(
    var id: Int = 0,
    var name: String = "",
    var contact: Contact? = Contact()
)