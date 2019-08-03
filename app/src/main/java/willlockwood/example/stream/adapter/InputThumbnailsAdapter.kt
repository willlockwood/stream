package willlockwood.example.stream.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.R
import willlockwood.example.stream.diffutil.InputThumbnailsDiffCallback
import willlockwood.example.stream.viewmodel.StreamViewModel


class InputThumbnailsAdapter internal constructor(
    context: Context,
    private var viewModel: StreamViewModel
    ) : RecyclerView.Adapter<InputThumbnailsAdapter.ThumbnailViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var uris = emptyList<String>()
    private var listener: OnItemClickListener? = null

    inner class ThumbnailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val imageButton: ImageButton = itemView.findViewById(R.id.imageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbnailViewHolder {
        val itemView = inflater.inflate(R.layout.row_input_thumbnail, parent, false)
        return ThumbnailViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ThumbnailViewHolder, position: Int) {
        val uri = uris[position]
        holder.imageView.setImageURI(Uri.parse(uri))
        holder.imageButton.setOnClickListener { view -> listener!!.onItemClick(view, position, uris, uri) }
    }

    internal fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, uris: List<String>, uri: String)
    }

    internal fun setUris(uris: List<String>) { updateUris(uris) }

    private fun updateUris(u: List<String>) {
        val diffCallback = InputThumbnailsDiffCallback(this.uris, u)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.uris = u
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = uris.size

}