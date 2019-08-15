package willlockwood.example.stream.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_table",
    indices = [Index(value = ["source"], name = "source", unique = true)]
)
data class StreamUser (
    val source: String,
    var name: String,
    var userName: String,
    val profilePictureUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}