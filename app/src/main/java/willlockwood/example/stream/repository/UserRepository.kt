package willlockwood.example.stream.repository

import androidx.lifecycle.LiveData
import willlockwood.example.stream.dao.UserDao
import willlockwood.example.stream.model.StreamUser

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun insertNewUser(user: StreamUser) { userDao.insert(user) }

    fun getUsers(): LiveData<List<StreamUser>> = userDao.getUsers()
}