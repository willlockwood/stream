package willlockwood.example.stream.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import willlockwood.example.stream.R
import java.io.File

class StreamItemPhotosAdapter internal constructor(
    private val context: Context
) : RecyclerView.Adapter<StreamItemPhotosAdapter.StreamPhotoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var uris = emptyList<String>()
    private var listener: OnItemClickListener? = null

    inner class StreamPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fullscreen_photo_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamPhotoViewHolder {
        val itemView = inflater.inflate(R.layout.row_stream_row_image, parent, false)
        return StreamPhotoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StreamPhotoViewHolder, position: Int) {
        val uri = Uri.parse(uris[position])

        val imageView = holder.imageView

        GlideApp.with(holder.itemView.context)
            .load(File(uri.path!!))
            .into(imageView)

        holder.imageView.setOnClickListener { view -> listener!!.onItemClick(view, position, ArrayList(uris)) }
    }

    internal fun setUris(uris: List<String>) {
        this.uris = uris
        notifyDataSetChanged()
    }

    internal fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, uris: ArrayList<String>)
    }

    override fun getItemCount() = uris.size
}

@GlideModule
class AppGlideModule : AppGlideModule()