package willlockwood.example.stream.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import willlockwood.example.stream.model.StreamUser

@Dao
interface UserDao {

    @Query("SELECT * from users")
    fun getUsers(): LiveData<List<StreamUser>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: StreamUser): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDefaultUser(user: StreamUser)

}