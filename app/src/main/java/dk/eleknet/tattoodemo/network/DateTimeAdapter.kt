package dk.eleknet.tattoodemo.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import org.joda.time.DateTime

class DateTimeAdapter {

    @ToJson
    fun toJson(value: DateTime?): String {
        var date = DateTime()
        if (value != null) date = value
        return date.toString("yyyy-MM-dd'T'HH:mm:ss+00:00")
    }

    @FromJson
    fun fromJson(reader: JsonReader): DateTime = DateTime.parse(reader.nextString())
}