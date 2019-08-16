package willlockwood.example.stream.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.R
import willlockwood.example.stream.diffutil.StreamDiffCallback
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.UserViewModel

class StreamListAdapter internal constructor(
    private val context: Context,
    private val streamVM: StreamViewModel,
    private val userVM: UserViewModel
) : RecyclerView.Adapter<StreamListAdapter.StreamViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var streams = emptyList<Stream>()

    inner class StreamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val streamText: TextView = itemView.findViewById(R.id.text)
        val recycler: RecyclerView = itemView.findViewById(R.id.recycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        val itemView = inflater.inflate(R.layout.row_stream, parent, false)
        return StreamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) {
        val stream = streams[position]

        val urisList: List<String>? = stream.imageUris?.split(", ")

        holder.recycler.visibility = View.GONE

        // Add images if there are images to add
        if (urisList != null) {
            holder.recycler.visibility = View.VISIBLE
            holder.recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val adapter = StreamItemPhotosAdapter(context)

            // Set click listener for photos in the stream
            adapter.setOnItemClickListener(object : StreamItemPhotosAdapter.OnItemClickListener {
                override fun onItemClick(view: View, position: Int, uris: ArrayList<String>) {
                    val bundle = Bundle()
                    bundle.putInt("position", position)
                    bundle.putStringArrayList("uris", uris)
                    view.findNavController().navigate(R.id.action_streams_to_streamsPhotosFullscreen, bundle, null, null)
                }
            })
            holder.recycler.adapter = adapter
            adapter.setUris(urisList)
        }

        holder.streamText.text = stream.text

        if (stream.threadable) {
            holder.streamText.setOnLongClickListener {
                streamVM.setStreamBeingThreaded(stream)
                true
            }
        }
    }

    internal fun isStreamDeleteableAtPosition(position: Int): Boolean {
        val stream = this.streams[position]
        return stream.deleteable
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