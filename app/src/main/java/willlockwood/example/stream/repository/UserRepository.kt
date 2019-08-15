package willlockwood.example.stream.repository

import androidx.lifecycle.LiveData
import willlockwood.example.stream.dao.UserDao
import willlockwood.example.stream.model.User

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun insertNewUser(user: User) { userDao.insert(user) }

    fun getUsers(): LiveData<List<User>> = userDao.getUsers()

    fun getUserBySource(source: String): LiveData<User> = userDao.getUserBySource(source)
}