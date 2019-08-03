package willlockwood.example.stream.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import willlockwood.example.stream.model.Tag

@Dao
interface TagDao {

//    @Transaction
//    @Query("SELECT * from tag_table ORDER BY id DESC ")
//    fun getAllTags(): LiveData<List<Tag>>
//
    @Query("SELECT * from tag_table ORDER BY position ASC ")
    fun getTagsOrderedByPosition(): LiveData<List<Tag>>
//
////    @Transaction
//    @Query("SELECT * from tag_table WHERE id = :id")
//    fun getTagById(id: Int): LiveData<Tag>
//
    @Query("SELECT * from tag_table WHERE name = :tagName")
    suspend fun getTagByName(tagName: String): Tag
//
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tag: Tag): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTags(tags: List<Tag>)
//
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDefaultTag(tag: Tag)
//
//    @Query("DELETE FROM tag_table")
//    suspend fun deleteAll()

    @Delete
    suspend fun deleteTags(vararg tags: Tag)
//
//    @Insert
//    suspend fun addTags(vararg tags: Tag)
//
    @Query("DELETE FROM tag_table WHERE type == 'default' ")
    suspend fun deleteDefaultTags()

//    @Query("SELECT DISTINCT id FROM tag_table" )
//    fun getUniqueTagIds(): LiveData<List<Int>>

}