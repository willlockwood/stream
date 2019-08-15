package willlockwood.example.stream.model

import androidx.room.*
import willlockwood.example.stream.UriConverters

@Entity(
    tableName = "streams",
    indices = [Index(value = ["tag"], name = "tag")],
    foreignKeys = [ForeignKey(
        entity = Tag::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("tag"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)]
)
data class Stream(
    var tag: String,
    var text: String,
    var deleteable: Boolean,

    @TypeConverters(UriConverters::class)
    var imageUris: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var streamId: Int = 0
}