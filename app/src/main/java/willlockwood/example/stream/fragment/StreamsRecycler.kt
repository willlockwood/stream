package willlockwood.example.stream.fragment

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Media
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.fragment_streams_streams.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import willlockwood.example.stream.DragToReorderCallback
import willlockwood.example.stream.R
import willlockwood.example.stream.SwipeToDeleteCallback
import willlockwood.example.stream.SwipeToTweetCallback
import willlockwood.example.stream.adapter.StreamsAdapter
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.TextToSpeechVM
import willlockwood.example.stream.viewmodel.UserViewModel
import java.io.File


class StreamsRecycler : Fragment() {

    // ViewModels
    lateinit var streamVM: StreamViewModel
    lateinit var userVM: UserViewModel
    lateinit var textToSpeechVM: TextToSpeechVM

    // Recycler
    lateinit var recyclerView: RecyclerView
    lateinit var streamAdapter: StreamsAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_streams_streams, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModels()

        setUpRecyclerView()

        observeStreams()
    }

    private fun setUpViewModels() {
        userVM = ViewModelProviders.of(activity!!).get(UserViewModel::class.java)
        streamVM = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
        textToSpeechVM = ViewModelProviders.of(activity!!).get(TextToSpeechVM::class.java)
    }

    private fun observeStreams() {
        streamVM.getFilteredStreams().observe(viewLifecycleOwner, Observer {
            streamAdapter.setStreams(it)
            recyclerView.scrollToPosition(streamAdapter.itemCount - 1)
        })
    }

    private fun setUpRecyclerView() {
        recyclerView = stream_recyclerView
        streamAdapter = StreamsAdapter(this.context!!, streamVM, textToSpeechVM)
        recyclerView.adapter = streamAdapter
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager


        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)
            }
        }

        val dragToReorderHandler = object : DragToReorderCallback(context!!) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return super.onMove(recyclerView, viewHolder, target)
            }
        }
        val itemTouchDragHelper = ItemTouchHelper(dragToReorderHandler)
        itemTouchDragHelper.attachToRecyclerView(recyclerView)

        // Swipe To Delete
        val swipeDeleteHandler = object : SwipeToDeleteCallback(context!!, "streams") {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                streamAdapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(recyclerView)

        // Swipe to Tweet
        val swipeTweetHandler = object : SwipeToTweetCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val streamToTweet = streamAdapter.streamAt(viewHolder.adapterPosition)

                if (streamToTweet.thread != null) {
                    GlobalScope.launch {
                        val streams = streamVM.returnStreamsByThreadId(streamToTweet.thread!!)
                        val firstStream = streams[0]
                        var followingStreams: List<Stream>? = null
                        if (streams.size > 1) {
                            followingStreams = streams.subList(1, streams.size - 1)
                        }
                        withContext(Dispatchers.Main) {
                            iterateTweet(firstStream, followingStreams, null, null)
                        }
                    }
                } else {
                    iterateTweet(streamToTweet, null, null, null)
                }

                streamVM.tweetStream(streamToTweet) // TODO: see if you can put this in the logic of the tweet function
            }
        }
        val itemTouchTweetHelper = ItemTouchHelper(swipeTweetHandler)
        itemTouchTweetHelper.attachToRecyclerView(recyclerView)
    }



    private fun getMimeType(file: File): String? {
        val ext = getExtension(file.getName())
        return if (!TextUtils.isEmpty(ext)) {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
        } else "application/octet-stream" // TODO: I don't know what "octet-stream" is, so maybe check that out
    }

    private fun getExtension(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val i = filename.lastIndexOf(".")
        return if (i < 0) "" else filename.substring(i + 1)
    }

    fun iterateTweet(stream: Stream, followingStreams: List<Stream>?, replyId: Long?, replyName: String?) {
//        var replyId: Long? = null
//        var replyName: String? = null
//        for (stream in streams) {
        var message = stream.text
        var mediaIds: String? = null

        val urisString = stream.imageUris
        if (urisString != null) {
            var uris = urisString.split(", ")
            if (uris.size > 4) {
                Toast.makeText(context, "Only tweeting first four images", Toast.LENGTH_SHORT).show()
                uris = uris.subList(0, 3)
            }
            val numberOfImages = uris.size
            var mediaIdsList = emptyList<String>()
            for (uriString in uris) {
                val imageFile = File(Uri.parse(uriString).path!!)
                val mimeType = getMimeType(imageFile)
                val requestBody = RequestBody.create(MediaType.parse(mimeType), imageFile)

                TwitterCore.getInstance().apiClient.mediaService.upload(requestBody, null, null).enqueue(object : retrofit2.Callback<Media> {
                    override fun onResponse(call: Call<Media>?, response: Response<Media>?) {
                        mediaIdsList = mediaIdsList.plus(response!!.body().mediaIdString)
                        if (mediaIdsList.size == numberOfImages) mediaIds = mediaIdsList.joinToString(",")
                    }
                    override fun onFailure(call: Call<Media>?, t: Throwable?) { Log.i("StreamsRecycler", "Tweet Failed") }
                })
            }
        }


        if (replyName != null) message = "@$replyName $message"

        Log.i("TWEETmediaIdsList", mediaIds.toString())
        Log.i("TWEETmessage", message)
        Log.i("TWEETreplyId", replyId.toString())
        Log.i("TWEETmediaIds", mediaIds.toString())

        val statusesService = TwitterCore.getInstance().apiClient.statusesService
        statusesService.update(message, replyId, null, null, null, null, null, null, mediaIds)
            .enqueue(object : Callback<Tweet>() {
                override fun success(result: Result<Tweet>?) {
                    Toast.makeText(context, "Tweet posted!", Toast.LENGTH_SHORT).show()

                    val nextReplyId = result?.data?.id
                    val nextReplyName = result?.data?.user?.screenName

                    stream.tweetId = nextReplyId
                    streamVM.updateStream(stream)

                    Log.i("result", nextReplyId.toString())
                    Log.i("result", nextReplyName.toString())

                    if (followingStreams != null) {
                        if (followingStreams.isNotEmpty()) {
                            val nextStream = followingStreams[0]
                            var nextFollowingStreams: List<Stream>? = null
                            if (followingStreams.size > 1) {
                                nextFollowingStreams = followingStreams.subList(1, followingStreams.size - 1)
                            }

                            iterateTweet(nextStream, nextFollowingStreams, nextReplyId, nextReplyName)
                        }
                    }

                    //TODO: update stream model to make it so that "tweetable" is just the tweet id, and update the tweet id here

                }
                override fun failure(exception: TwitterException) {
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }
}