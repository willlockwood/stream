package willlockwood.example.stream.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_table",
    indices = [Index(value = ["userName"], name = "userName", unique = true)]
)
data class StreamUser (
    var name: String,
    var userName: String,
    val profilePictureUrl: String,
    val socialNetwork: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

enum class SocialNetwork {
    Facebook, Twitter
}
