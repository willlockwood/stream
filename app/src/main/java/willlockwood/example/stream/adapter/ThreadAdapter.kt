package willlockwood.example.stream.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.R
import willlockwood.example.stream.diffutil.StreamDiffCallback
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewholder.StreamViewHolder
import willlockwood.example.stream.viewholder.ThreadViewHolder
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.UserViewModel

class ThreadAdapter internal constructor(
    private val context: Context,
    private val streamVM: StreamViewModel,
    private val userVM: UserViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var streams = emptyList<Stream>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CellType.SINGLE_STREAM.ordinal -> StreamViewHolder(inflater.inflate(R.layout.row_stream_in_thread, parent, false))
            CellType.STACK.ordinal -> ThreadViewHolder(inflater.inflate(R.layout.row_stream_stack, parent, false))
            else -> StreamViewHolder(inflater.inflate(R.layout.row_stream_photos, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CellType.SINGLE_STREAM.ordinal -> {
                val streamViewHolder = holder as StreamViewHolder
                streamViewHolder.bindView(streams[position], streamVM)
            }
            CellType.STACK.ordinal -> {
                val stackViewHolder = holder as ThreadViewHolder
                stackViewHolder.bindView(streams[position], streamVM)
            }
        }
    }

    internal fun isStreamDeleteableAtPosition(position: Int): Boolean {
        val stream = this.streams[position]
        return stream.deleteable
    }

    override fun getItemViewType(position: Int): Int {
        val stream = this.streams[position]
        if (stream.thread == null) {
            return CellType.SINGLE_STREAM.ordinal
        } else {
            return CellType.STACK.ordinal
        }
    }

    enum class CellType(viewType: Int) {
        SINGLE_STREAM(0),
        STACK(1),
        SINGLE_STREAMS_WITH_PHOTOS(2)
    }

    internal fun removeAt(index: Int) { streamVM.deleteStream(this.streams[index]) }

    internal fun streamAt(index: Int): Stream = this.streams[index]

    internal fun setStreams(streams: List<Stream>) { updateStreams(streams) }

    private fun updateStreams(s: List<Stream>) {
        val diffCallback = StreamDiffCallback(this.streams, s)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.streams = s

        if (this.streams.isEmpty()) {
            val defaultStream = Stream(streamVM.getCurrentTag().value!!.name,"Stream something!",
                deleteable = false,
                tweetable = false,
                threadable = false
            )
            this.streams = listOf(defaultStream)
        }

        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = streams.size
}