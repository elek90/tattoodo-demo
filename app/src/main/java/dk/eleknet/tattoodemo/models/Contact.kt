package dk.eleknet.tattoodemo.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Contact(
    var phone: String = "",
    var email: String = "",
    var website: String = ""
)