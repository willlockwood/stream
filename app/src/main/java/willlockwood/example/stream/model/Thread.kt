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
    var title: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}