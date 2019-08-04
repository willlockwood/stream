package willlockwood.example.stream.model

import androidx.room.*
import willlockwood.example.stream.UriConverters

@Entity(
    tableName = "stream_table",
    indices = [Index(value = ["tagName"], name = "tagName")],
    foreignKeys = [ForeignKey(
        entity = Tag::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("tagName"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)]
)
data class Stream(
    var tagName: String,
    var text: String,

    @TypeConverters(UriConverters::class)
    var imageUris: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var streamId: Int = 0
}