package willlockwood.example.stream.repository

import androidx.lifecycle.LiveData
import willlockwood.example.stream.dao.StreamDao
import willlockwood.example.stream.dao.TagDao
import willlockwood.example.stream.dao.ThreadDao
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.model.Tag
import willlockwood.example.stream.model.Thread

class StreamRepository(
    private val streamDao: StreamDao,
    private val tagDao: TagDao,
    private val threadDao: ThreadDao
) {

    fun getAllTags() : LiveData<List<Tag>> = tagDao.getTagsOrderedByPosition()
    suspend fun getTagByName(tag: String): Tag = tagDao.getTagByName(tag)

//    fun insertStream(tagName: String, text: String) { streamDao.insert(Stream(tagName, text)) }
    fun insertStream(stream: Stream) { streamDao.insert(stream) }

    fun insertThread(thread: Thread): Long { return streamDao.insertThread(thread) }

    suspend fun updateStream(stream: Stream) { streamDao.updateStream(stream) }
    suspend fun updateStreams(streams: List<Stream>) { streamDao.updateStreams(streams) }

    suspend fun updateThread(thread: Thread) { threadDao.updateThread(thread) }

    suspend fun updateTags(tags: List<Tag>) { tagDao.updateTags(tags) }

    suspend fun deleteTags(vararg tags: Tag) = tagDao.deleteTags(*tags)

    suspend fun deleteStreams(vararg streams: Stream) = streamDao.deleteStreams(*streams)

    fun getStreamsByTag(tag: Tag): LiveData<List<Stream>> {
        return when (tag.name) {
            "All" -> streamDao.getAllStreams()
            else -> streamDao.getStreamsByTag(tag.name)
        }
    }

    fun getStreamsByThread(thread: Thread?): LiveData<List<Stream>>? {
        return when (thread) {
            null -> streamDao.getStreamsByThread(-1)
            else -> streamDao.getStreamsByThread(thread.id)
        }
    }

    suspend fun getThreadById(id: Int): Thread? = threadDao.getThreadById(id)

    suspend fun insertNewTag(): Long {
        var newTagNumber = 1
        val newTag = Tag("New Tag")
        while (tagDao.insert(newTag).toInt() == -1) {
            newTagNumber++
            newTag.name = "New Tag $newTagNumber"
        }
        return tagDao.insert(newTag)
    }

//    suspend fun insertNewUser(user: User) { userDao.insert(user) }
//
//    fun getUsers() { userDao.getUsers() }
}