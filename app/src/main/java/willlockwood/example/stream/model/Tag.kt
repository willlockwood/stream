package willlockwood.example.stream.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
    indices = [Index(value = ["name"], name = "stream_tag", unique = true)]
)
data class Tag (
    var name: String,
    var position: Int? = null,
    var moveable: Boolean = true,
    var deleteable: Boolean = true,
    var renameable: Boolean = true,
    var type: String = "custom"

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    init {
        position = when (name) {
            "All" -> 0
            "About" -> 1000
            else -> 999
        }
        if (name in listOf("All", "About")) {
            deleteable = false
            moveable = false
            renameable = false
            type = "default"
        }
    }
}