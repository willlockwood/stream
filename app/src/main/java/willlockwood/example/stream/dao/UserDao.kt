package willlockwood.example.stream.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import willlockwood.example.stream.model.User

@Dao
interface UserDao {

    @Query("SELECT * from users")
    fun getUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertDefaultUser(user: User)

}