package willlockwood.example.stream.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.diffutil.StreamDiffCallback
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewholder.StreamInThreadViewHolder
import willlockwood.example.stream.viewholder.StreamViewHolder
import willlockwood.example.stream.viewholder.ThreadViewHolder
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.TextToSpeechVM
import willlockwood.example.stream.viewmodel.UserViewModel
import java.util.*


class StreamsAdapter internal constructor(
    private val context: Context,
    private val streamVM: StreamViewModel,
    private val userVM: UserViewModel,
    private val textToSpeechVM: TextToSpeechVM
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var streams = emptyList<Stream>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CellType.SINGLE_STREAM.ordinal -> StreamViewHolder(inflater.inflate(willlockwood.example.stream.R.layout.row_stream, parent, false))
            CellType.SINGLE_STREAM_IN_THREAD.ordinal -> StreamInThreadViewHolder(inflater.inflate(willlockwood.example.stream.R.layout.row_stream_in_thread, parent, false))
            CellType.STACK.ordinal -> ThreadViewHolder(inflater.inflate(willlockwood.example.stream.R.layout.row_stream_stack, parent, false))
            else -> StreamViewHolder(inflater.inflate(willlockwood.example.stream.R.layout.row_stream_photos, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CellType.SINGLE_STREAM.ordinal -> {
                val streamViewHolder = holder as StreamViewHolder
                streamViewHolder.bindView(streams[position], streamVM, textToSpeechVM)
            }
            CellType.STACK.ordinal -> {
                val stackViewHolder = holder as ThreadViewHolder
                stackViewHolder.bindView(streams[position], streamVM, textToSpeechVM)
            }
            CellType.SINGLE_STREAM_IN_THREAD.ordinal -> {
                val streamInThreadViewHolder = holder as StreamInThreadViewHolder
                streamInThreadViewHolder.bindView(streams[position], streamVM, textToSpeechVM)
            }
        }
    }

    internal fun isStreamDeleteableAtPosition(position: Int): Boolean {
        val stream = this.streams[position]
        return stream.deleteable
    }

    override fun getItemViewType(position: Int): Int {
        val stream = this.streams[position]
        if (streamVM.getCurrentThread().value != null) {
            return CellType.SINGLE_STREAM_IN_THREAD.ordinal
        } else if (stream.thread == null) {
            return CellType.SINGLE_STREAM.ordinal
        } else {
            return CellType.STACK.ordinal
        }
    }

    enum class CellType(viewType: Int) {
        SINGLE_STREAM(0),
        STACK(1),
        SINGLE_STREAMS_WITH_PHOTOS(2),
        SINGLE_STREAM_IN_THREAD(3)
    }

    internal fun removeAt(index: Int) { streamVM.deleteStream(this.streams[index]) }

    internal fun streamAt(index: Int): Stream = this.streams[index]

    internal fun setStreams(streams: List<Stream>) { updateStreams(streams) }

    private fun updateStreams(s: List<Stream>) {

        Log.i("streams", this.streams.toString())
        Log.i("s", s.toString())

        val diffCallback = StreamDiffCallback(this.streams, s)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        Log.i("diffResult", diffResult.toString())

        diffResult.dispatchUpdatesTo(this)

        this.streams = s

        if (this.streams.isEmpty()) {
            val defaultStream = Stream(streamVM.getCurrentTag().value!!.name,"Stream something!",
                deleteable = false,
                tweetable = false,
                threadable = false
            )
            this.streams = listOf(defaultStream)
        }

    }

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(streams, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(streams, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun getItemCount() = streams.size
}