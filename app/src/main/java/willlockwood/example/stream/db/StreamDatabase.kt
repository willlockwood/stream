package willlockwood.example.stream.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import willlockwood.example.stream.UriConverters
import willlockwood.example.stream.dao.StreamDao
import willlockwood.example.stream.dao.TagDao
import willlockwood.example.stream.dao.UserDao
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.model.User
import willlockwood.example.stream.model.Tag

@Database(
    entities = [Stream::class, Tag::class, User::class],
    version = 5
)
@TypeConverters(UriConverters::class)
abstract class StreamDatabase : RoomDatabase() {

    abstract fun streamDao(): StreamDao
    abstract fun tagDao(): TagDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: StreamDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): StreamDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StreamDatabase::class.java,
                    "stream_database")
                    .addCallback(StreamDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class StreamDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.streamDao(), database.tagDao(), database.userDao())
                    }
                }
            }
        }
        suspend fun populateDatabase(streamDao: StreamDao, tagDao: TagDao, userDao: UserDao) {
            streamDao.deleteDefaultStreams()
            tagDao.deleteDefaultTags()
            userDao.insert(User("guest", "guest", "", "stream"))

            tagDao.insertDefaultTag(Tag("All"))
            tagDao.insertDefaultTag(Tag("About"))

            val aboutStreams = listOf("Thank you for using Stream",
                "To learn more about updates or give feedback, reach out to @vidythatte on twitter",
                "Made with love, brains and coffee in San Francisco")
            for (text in aboutStreams) {
                streamDao.insertAboutStream(Stream("About", text, false))
            }
        }
    }
}