package willlockwood.example.stream.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.model.Thread

@Dao
interface StreamDao {

    @Query("SELECT * from streams WHERE tag != 'About' AND positionInThread == 0")
    fun getAllStreams(): LiveData<List<Stream>>

    @Query("SELECT * FROM streams WHERE thread == :id")
    fun getStreamsByThread(id: Int): LiveData<List<Stream>>

    @Query("SELECT * FROM streams WHERE thread == :id")
    suspend fun returnStreamsByThread(id: Int): List<Stream>

    @Query("SELECT * FROM streams WHERE tag  == :tagName AND positionInThread == 0")
    fun getStreamsByTag(tagName: String): LiveData<List<Stream>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertThread(thread: Thread): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(stream: Stream)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStreams(streams: List<Stream>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStream(stream: Stream)

    @Query("DELETE  FROM streams WHERE tag == 'About' ")
    suspend fun deleteDefaultStreams()

    @Query("DELETE FROM streams ")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteStreams(vararg streams: Stream)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAboutStream(stream: Stream)
}