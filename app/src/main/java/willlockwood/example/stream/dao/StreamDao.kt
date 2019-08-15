package willlockwood.example.stream.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import willlockwood.example.stream.model.Stream

@Dao
interface StreamDao {

    @Query("SELECT * from streams WHERE tagName != 'About' ")
    fun getAllStreams(): LiveData<List<Stream>>

    @Query("SELECT * FROM streams WHERE tagName == :tagName")
    fun getStreamsByTag(tagName: String): LiveData<List<Stream>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(stream: Stream)

    @Query("DELETE  FROM streams WHERE tagName == 'About' ")
    suspend fun deleteDefaultStreams()

    @Query("DELETE FROM streams ")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteStreams(vararg streams: Stream)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAboutStream(stream: Stream)
}