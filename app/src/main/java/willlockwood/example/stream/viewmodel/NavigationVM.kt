package willlockwood.example.stream.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import willlockwood.example.stream.utils.Event

class NavigationVM(application: Application) : AndroidViewModel(application) {

    private val newDestination = MutableLiveData<Event<Int>>()

    fun getNewDestination(): LiveData<Event<Int>> {
        return newDestination
    }

    fun setNewDestination(destinationId: Int) {
        newDestination.value = Event(destinationId)
    }
}