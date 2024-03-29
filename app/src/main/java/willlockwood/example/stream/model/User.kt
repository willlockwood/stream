package willlockwood.example.stream.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["source"], name = "source", unique = true)]
)
data class User (
    val source: String,
    var name: String,
    var userName: String,
    val profilePictureUrl: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}