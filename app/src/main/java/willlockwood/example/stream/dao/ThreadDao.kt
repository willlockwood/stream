package willlockwood.example.stream.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import willlockwood.example.stream.model.Thread

@Dao
interface ThreadDao {

//    @Query("SELECT * from streams WHERE tag != 'About' ")
//    fun getAllStreams(): LiveData<List<Stream>>



//    @Query("SELECT * from streams WHERE tag != 'About' AND positionInThread == 0")
//    fun getAllStreams(): LiveData<List<Stream>>
//
    @Query("SELECT * FROM threads WHERE id == :id")
    suspend fun getThreadById(id: Int): Thread
//
//    @Query("SELECT * FROM streams WHERE tag  == :tagName AND positionInThread == 0")
//    fun getStreamsByTag(tagName: String): LiveData<List<Stream>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertThread(thread: Thread): Long
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insert(stream: Stream)
//
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateThread(thread: Thread)
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun updateStream(stream: Stream)
//
//    @Query("DELETE  FROM streams WHERE tag == 'About' ")
//    suspend fun deleteDefaultStreams()
//
//    @Query("DELETE FROM streams ")
//    suspend fun deleteAll()
//
//    @Delete
//    suspend fun deleteStreams(vararg streams: Stream)
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insertAboutStream(stream: Stream)
}