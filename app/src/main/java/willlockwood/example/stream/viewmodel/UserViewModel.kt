package willlockwood.example.stream.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import willlockwood.example.stream.db.StreamDatabase
import willlockwood.example.stream.model.StreamUser
import willlockwood.example.stream.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    val userDao = StreamDatabase.getDatabase(application, viewModelScope).userDao()
    private val repository = UserRepository(userDao)

    private var currentUser = MutableLiveData<StreamUser>()

    init {
        viewModelScope.launch(Dispatchers.IO) { currentUser.postValue(StreamUser("blah", "blah", "blah", "blah")) }
//        currentUser.postValue(getAllUsers().value!![0])
    }

//    private val users: LiveData<List<StreamUser>> by lazy { repository.getUsers() }
    private fun getAllUsers() = repository.getUsers()

    fun setCurrentUser(user: StreamUser) { currentUser.value = user }
    fun getCurrentUser() = currentUser

    fun insertNewUser(user: StreamUser) = viewModelScope.launch(Dispatchers.IO) { repository.insertNewUser(user)}


}