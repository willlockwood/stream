package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.twitter.sdk.android.core.models.Tweet
import kotlinx.android.synthetic.main.fragment_streams_streams.*
import willlockwood.example.stream.R
import willlockwood.example.stream.SwipeToDeleteCallback
import willlockwood.example.stream.SwipeToTweetCallback
import willlockwood.example.stream.adapter.StreamListAdapter
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.UserViewModel

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
        return inflater.inflate(R.layout.fragment_streams_streams, container, false)
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
                tweet(streamToTweet.text)
                streamVM.tweetStream(streamToTweet)
//                streamAdapter.removeAt(viewHolder.adapterPosition)

            }
        }
        val itemTouchTweetHelper = ItemTouchHelper(swipeTweetHandler)
        itemTouchTweetHelper.attachToRecyclerView(recyclerView)
    }


    fun tweet(message: String) {
        val statusesService = TwitterCore.getInstance().apiClient.statusesService
//        val context = this
        statusesService.update(message, null, null, null, null, null, null, null, null)
            .enqueue(object : Callback<Tweet>() {
                override fun success(result: Result<Tweet>?) {
//                    Toast.makeText(context,R.string.tweet_posted,Toast.LENGTH_SHORT).show()
                }

                override fun failure(exception: TwitterException) {
//                    Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_SHORT).show()
                }
            })
//        postEditText.setText("")
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