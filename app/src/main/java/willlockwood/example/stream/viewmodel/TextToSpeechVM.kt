package willlockwood.example.stream.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class TextToSpeechVM(application: Application) : AndroidViewModel(application) {


    private var textToSpeak = MutableLiveData<List<String>>()



    fun getTextToSpeak() = textToSpeak

    fun setTextToSpeak(textList: List<String>) {
        textToSpeak.postValue(textList)
    }
    fun setTextToSpeak(text: String) {
        val returnList = emptyList<String>()
        textToSpeak.postValue(returnList.plus(text))
    }


//    private var speechQueue = MutableLiveData<List<String>>()
//
//    init {
//        viewModelScope.launch(Dispatchers.IO) { currentUser.postValue(User("blah", "blah", "blah", "blah")) }
//    }
//
//    private fun getSpeechQueue() = speechQueue
//
//    private fun addToSpeechQueue(text: String)
//
//
//
//    private fun getAllUsers() = repository.getUsers()
//
//    fun getTwitterUser(): LiveData<User> = repository.getUserBySource("twitter")
//
//    private fun getUserBySource(source: String) = repository.getUserBySource(source)
//
//    fun setCurrentUser(user: User) { currentUser.value = user }
//    fun getCurrentUser() = currentUser
//
//    fun insertNewUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
//        Log.i("insertNewUser", user.toString())
//        repository.insertNewUser(user)
//    }
}