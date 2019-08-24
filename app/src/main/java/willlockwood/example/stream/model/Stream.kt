package willlockwood.example.stream.model

import androidx.room.*
import willlockwood.example.stream.UriConverters

@Entity(
    tableName = "streams",
    indices = [Index(value = ["tag"], name = "tag")],
    foreignKeys = [
        ForeignKey(
            entity = Tag::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("tag"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE),
        ForeignKey(
            entity = Stream::class,
            parentColumns = arrayOf("streamId"),
            childColumns = arrayOf("isAReplyToId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE),
        ForeignKey(
            entity = Stream::class,
            parentColumns = arrayOf("streamId"),
            childColumns = arrayOf("isRepliedById"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE),
        ForeignKey(
            entity = Thread::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("thread"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class Stream(
    var tag: String,
    var text: String,
    var deleteable: Boolean,
    var tweetable: Boolean,
    var threadable: Boolean,
    var tweetId: Long? = null,
    var tweeted: Boolean = false,
    var positionInThread: Int? = 0,
    var thread: Int? = null,
    var isAReplyToId: Int? = null,
    var isRepliedById: Int? = null,

    @TypeConverters(UriConverters::class)
    var imageUris: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var streamId: Int = 0

    override fun equals(other: Any?): Boolean {

        other as Stream

        return when {
            this.streamId != other.streamId -> false
            this.tag != other.tag -> false
            this.text != other.text -> false
            this.tweetId != other.tweetId -> false
            this.positionInThread != other.positionInThread -> false
            this.thread != other.thread -> false
            else -> true
        }
//        return super.equals(other)
    }

    // TODO: OVERRIDE HASHMAP FUNCTION
}