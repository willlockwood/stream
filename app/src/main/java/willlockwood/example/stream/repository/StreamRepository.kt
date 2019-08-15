package willlockwood.example.stream.repository

import androidx.lifecycle.LiveData
import willlockwood.example.stream.dao.StreamDao
import willlockwood.example.stream.dao.TagDao
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.model.Tag

class StreamRepository(
    private val streamDao: StreamDao,
    private val tagDao: TagDao
) {

    fun getAllTags() : LiveData<List<Tag>> = tagDao.getTagsOrderedByPosition()
    suspend fun getTagByName(tag: String): Tag = tagDao.getTagByName(tag)

//    fun insertStream(tagName: String, text: String) { streamDao.insert(Stream(tagName, text)) }
    fun insertStream(stream: Stream) { streamDao.insert(stream) }

    suspend fun updateTags(tags: List<Tag>) { tagDao.updateTags(tags) }
    suspend fun deleteTags(vararg tags: Tag) = tagDao.deleteTags(*tags)

    suspend fun deleteStreams(vararg streams: Stream) = streamDao.deleteStreams(*streams)

    fun getStreamsByTag(tag: Tag): LiveData<List<Stream>> {
        return when (tag.name) {
            "All" -> streamDao.getAllStreams()
            else -> streamDao.getStreamsByTag(tag.name)
        }
    }

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