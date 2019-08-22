package willlockwood.example.stream.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "threads",
    foreignKeys = [ForeignKey(
        entity = Tag::class,
        parentColumns = arrayOf("name"),
        childColumns = arrayOf("tag"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)]
    )
data class Thread (
    var tag: String,
    var title: String = "Untitled Thread",
    var size: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}