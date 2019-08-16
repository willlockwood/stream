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
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import willlockwood.example.stream.SwipeToDeleteCallback
import willlockwood.example.stream.SwipeToTweetCallback
import willlockwood.example.stream.adapter.StreamListAdapter
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.UserViewModel
import java.io.File


class StreamsRecycler : Fragment() {

    // ViewModels
    lateinit var streamVM: StreamViewModel
    lateinit var userVM: UserViewModel

    // Recycler
    lateinit var recyclerView: RecyclerView
    lateinit var streamAdapter: StreamListAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(willlockwood.example.stream.R.layout.fragment_streams_streams, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModels()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView = stream_recyclerView
        streamAdapter = StreamListAdapter(this.context!!, streamVM, userVM)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = streamAdapter

        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)
            }
        }

        val swipeDeleteHandler = object : SwipeToDeleteCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                streamAdapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(recyclerView)

        val swipeTweetHandler = object : SwipeToTweetCallback(context!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val streamToTweet = streamAdapter.streamAt(viewHolder.adapterPosition)
                tweet(streamToTweet)
                streamVM.tweetStream(streamToTweet)
//                streamAdapter.removeAt(viewHolder.adapterPosition)

            }
        }
        val itemTouchTweetHelper = ItemTouchHelper(swipeTweetHandler)
        itemTouchTweetHelper.attachToRecyclerView(recyclerView)
    }

    private fun getMimeType(file: File): String? {
        val ext = getExtension(file.getName())
        return if (!TextUtils.isEmpty(ext)) {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
        } else "application/octet-stream"
        // TODO: I don't know what "octet-stream" is, so maybe check that out
    }

    private fun getExtension(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val i = filename.lastIndexOf(".")
        return if (i < 0) "" else filename.substring(i + 1)
    }

    fun executeTweet(message: String, mediaIds: String?) {
        val statusesService = TwitterCore.getInstance().apiClient.statusesService
        statusesService.update(message, null, null, null, null, null, null, null, mediaIds)
            .enqueue(object : Callback<Tweet>() {
                override fun success(result: Result<Tweet>?) {
                    Toast.makeText(context, "Tweet posted!", Toast.LENGTH_SHORT).show()
                }
                override fun failure(exception: TwitterException) {
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun tweet(stream: Stream) {
        val message = stream.text
        val urisString = stream.imageUris

        if (urisString != null) {
            val uris = urisString.split(", ")
            val numberOfImages = uris.size
            when {
                uris.size > 4 -> Log.i("more than 4 images", "at streamsRecycler")
                uris.isNotEmpty() -> Log.i("more than 0 images", "at streamsRecycler")
                else -> Log.i("other number of images","at streamsRecycler")
            }

            var mediaIdsList = emptyList<String>()

            for (uriString in uris) {
                val imageFile = File(Uri.parse(uriString).path!!)
                val mimeType = getMimeType(imageFile)
                val requestBody = RequestBody.create(MediaType.parse(mimeType), imageFile)

                TwitterCore.getInstance().apiClient.mediaService.upload(requestBody, null, null)
                    .enqueue(object : retrofit2.Callback<Media> {
                        override fun onResponse(call: Call<Media>?, response: Response<Media>?) {
                            mediaIdsList = mediaIdsList.plus(response!!.body().mediaIdString)
                            if (mediaIdsList.size == numberOfImages) {
                                executeTweet(message, mediaIdsList.joinToString(","))
                            }
                        }

                        override fun onFailure(call: Call<Media>?, t: Throwable?) {
                            Log.i("failure", "here")
                        }
                    })
            }
        } else {
            executeTweet(message, null)
        }
    }

    private fun setUpViewModels() {
        userVM = ViewModelProviders.of(activity!!).get(UserViewModel::class.java)

        streamVM = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
        streamVM.getFilteredStreams().observe(viewLifecycleOwner, Observer {
            streamAdapter.setStreams(it)
            recyclerView.scrollToPosition(streamAdapter.itemCount - 1)
        })
    }

}