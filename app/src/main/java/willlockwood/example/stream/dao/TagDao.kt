package willlockwood.example.stream.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import willlockwood.example.stream.model.Tag

@Dao
interface TagDao {

    @Query("SELECT * from tags ORDER BY position ASC ")
    fun getTagsOrderedByPosition(): LiveData<List<Tag>>

    @Query("SELECT * from tags WHERE name = :tagName")
    suspend fun getTagByName(tagName: String): Tag
//
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: Tag): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTag(tag: Tag)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTags(tags: List<Tag>)
//
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDefaultTag(tag: Tag)

    @Delete
    suspend fun deleteTags(vararg tags: Tag)

    @Query("DELETE FROM tags WHERE type == 'default' ")
    suspend fun deleteDefaultTags()

}