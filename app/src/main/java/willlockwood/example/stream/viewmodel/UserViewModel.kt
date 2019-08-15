package willlockwood.example.stream.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import willlockwood.example.stream.db.StreamDatabase
import willlockwood.example.stream.model.User
import willlockwood.example.stream.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val userDao = StreamDatabase.getDatabase(application, viewModelScope).userDao()
    private val repository = UserRepository(userDao)

    private var currentUser = MutableLiveData<User>()

    init {
        viewModelScope.launch(Dispatchers.IO) { currentUser.postValue(User("blah", "blah", "blah", "blah")) }
//        currentUser.postValue(getAllUsers().value!![0])
    }

//    private val users: LiveData<List<User>> by lazy { repository.getUsers() }
    private fun getAllUsers() = repository.getUsers()

    fun getTwitterUser(): LiveData<User> = repository.getUserBySource("twitter")

    private fun getUserBySource(source: String) = repository.getUserBySource(source)

    fun setCurrentUser(user: User) { currentUser.value = user }
    fun getCurrentUser() = currentUser

    fun insertNewUser(user: User) = viewModelScope.launch(Dispatchers.IO) { repository.insertNewUser(user)}


}