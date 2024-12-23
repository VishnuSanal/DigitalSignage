import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

data class Announcement(
    val title: String = "Loading...",
    val message: String? = null,
    val imagePath: String? = null,
    val videoPath: String? = null,
) {
    constructor() : this("Loading...")
}

class AnnouncementAdapter : TypeAdapter<Announcement>() {
    override fun write(out: JsonWriter, value: Announcement) {
        out.beginObject()
        out.name("title").value(value.title)
        out.name("message").value(value.message)
        out.name("imagePath").value(value.imagePath)
        out.name("videoPath").value(value.videoPath)
        out.endObject()
    }

    override fun read(input: JsonReader): Announcement {
        var title: String = "Loading..."
        var message: String? = null
        var imagePath: String? = null
        var videoPath: String? = null

        input.beginObject()
        while (input.hasNext()) {
            when (input.nextName()) {
                "title" -> title = input.nextString()
                "message" -> message = input.nextString()
                "imagePath" -> imagePath = input.nextString()
                "videoPath" -> videoPath = input.nextString()
            }
        }
        input.endObject()
        return Announcement(title, message, imagePath, videoPath)
    }
}
