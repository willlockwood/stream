package willlockwood.example.stream.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import willlockwood.example.stream.db.StreamDatabase
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.model.Tag
import willlockwood.example.stream.model.Thread
import willlockwood.example.stream.repository.StreamRepository

class StreamViewModel(application: Application) : AndroidViewModel(application) {

    private val streamDao = StreamDatabase.getDatabase(application, viewModelScope).streamDao()
    private val tagDao = StreamDatabase.getDatabase(application, viewModelScope).tagDao()
    private val threadDao = StreamDatabase.getDatabase(application, viewModelScope).threadDao()
    private val repository = StreamRepository(streamDao, tagDao, threadDao)

    private var currentTag = MutableLiveData<Tag>()
//    private var imageThumbnailUris = MutableLiveData<Array<String>>(emptyArray())
//    private var thumbnailUris = MutableLiveData<List<String>>(emptyArray<>())
    private var thumbnailUris = MutableLiveData<Array<String>>()
    private var imageThumbnailUris = MutableLiveData<String>()
    private var filteredStreams: LiveData<List<Stream>>
    private var threadStreams: LiveData<List<Stream>>

    private var currentThread = MutableLiveData<Thread>()
    private var threadBeingEdited = MutableLiveData<Int>()
    private var streamBeingRepliedTo = MutableLiveData<Stream>()
    private var previousStreamBeingRepliedTo = MutableLiveData<Stream>()

    init {
        viewModelScope.launch(Dispatchers.IO) { currentTag.postValue(getTagByName("All")) }

        filteredStreams = Transformations.switchMap(currentTag) { tag -> repository.getStreamsByTag(tag) }
//        threadStreams = Transformations.switchMap(threadBeingEdited) { id -> repository.getStreamsByThread(id)}
        threadStreams = Transformations.switchMap(currentThread) { thread -> repository.getStreamsByThread(thread)}
    }

    private val tags: LiveData<List<Tag>> by lazy { repository.getAllTags() }

    fun getAllTags() = tags
    fun getFilteredStreams() = filteredStreams
    fun getThreadStreams() = threadStreams

    fun setCurrentTag(tag: Tag) { currentTag.value = tag }
    fun getCurrentTag() = currentTag
    private suspend fun getTagByName(tag: String) = repository.getTagByName(tag)
    fun insertStream(stream: Stream) = viewModelScope.launch(Dispatchers.IO) { repository.insertStream(stream) }

    fun newThreadFromStream(stream: Stream) = viewModelScope.launch(Dispatchers.IO) {
        val newThreadId = repository.insertThread(Thread(stream.tag)).toInt()
        stream.thread = newThreadId
        updateStream(stream)
        setCurrentThread(newThreadId)
//        setThreadBeingEdited(newThreadId)
    }

    suspend fun getThreadById(id: Int): Thread? {
        return repository.getThreadById(id)
    }

    fun updateThread(thread: Thread) = viewModelScope.launch(Dispatchers.IO) { repository.updateThread(thread) }

    fun insertNewTag() = viewModelScope.launch(Dispatchers.IO) { repository.insertNewTag() }

    fun updateTags(tags: List<Tag>) = viewModelScope.launch(Dispatchers.IO) { repository.updateTags(tags) }
    fun updateStreams(streams: List<Stream>) = viewModelScope.launch(Dispatchers.IO) { repository.updateStreams(streams) }
    fun updateStream(stream: Stream) = viewModelScope.launch(Dispatchers.IO) { repository.updateStream(stream) }

    fun deleteTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO) { repository.deleteTags(tag) }
    fun deleteStream(stream: Stream) = viewModelScope.launch(Dispatchers.IO) { repository.deleteStreams(stream) }

    fun tweetStream(stream: Stream) {
        stream.tweeted = true
        updateStream(stream)
    }

    fun getThumbnailUris() = thumbnailUris
    fun addThumbnailUris(uri: String) {
        val currentArray = thumbnailUris.value
        val newArray: Array<String>
        newArray = currentArray?.plus(uri) ?: arrayOf(uri)
        thumbnailUris.value = newArray
    }
    fun clearThumbnailUris() { thumbnailUris.value = null }
    fun deleteThumbnailUris(uri: String) {
        val currentArray = thumbnailUris.value
        val newArray = currentArray!!.filter { it != uri }.toTypedArray()
        if (newArray.isNotEmpty()) {
            thumbnailUris.value = newArray
        } else {
            thumbnailUris.value = null
        }
    }

    suspend fun setCurrentThread(id: Int?) {
        if (id != null) {
            val thread = getThreadById(id)
            currentThread.postValue(thread)
        } else {
            currentThread.postValue(null)
        }
    }
    fun getCurrentThread() = currentThread

    fun setThreadBeingEdited(id: Int?) {
//        threadBeingEdited.value = id
        threadBeingEdited.postValue(id)
    }
    fun getThreadBeingEdited() = threadBeingEdited

    fun setStreamBeingThreaded(stream: Stream?) {
        previousStreamBeingRepliedTo.value = streamBeingRepliedTo.value
        Log.i("prevStreamBeingReplied", previousStreamBeingRepliedTo.value.toString())
        Log.i("streamBeingRepliedTo", stream.toString())
        streamBeingRepliedTo.value = stream
    }
    fun getStreamBeingThreaded() = streamBeingRepliedTo
    fun getPreviousStreamBeingThreaded() = previousStreamBeingRepliedTo
}