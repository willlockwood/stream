package willlockwood.example.stream.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import willlockwood.example.stream.model.Stream

@Dao
interface StreamDao {

    @Query("SELECT * from stream_table WHERE tagName != 'About' ")
    fun getAllStreams(): LiveData<List<Stream>>

    @Query("SELECT * FROM stream_table WHERE tagName == :tagName")
    fun getStreamsByTag(tagName: String): LiveData<List<Stream>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(stream: Stream)

    @Query("DELETE  FROM stream_table WHERE tagName == 'About' ")
    suspend fun deleteDefaultStreams()

    @Query("DELETE FROM stream_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAboutStream(stream: Stream)
}