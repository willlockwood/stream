package willlockwood.example.stream.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.R
import willlockwood.example.stream.diffutil.TagDiffCallback
import willlockwood.example.stream.model.Tag
import willlockwood.example.stream.viewmodel.StreamViewModel


class TagAdapter internal constructor(
    private var context: Context,
    private var viewModel: StreamViewModel
    ) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tags = emptyList<Tag>()
    private var currentTag: Tag? = viewModel.getCurrentTag().value

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagButton: Button = itemView.findViewById(R.id.tag_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val itemView = inflater.inflate(R.layout.row_tag, parent, false)
        return TagViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]

        val currentTag = viewModel.getCurrentTag().value
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (tag == viewModel.getCurrentTag().value) {
                holder.tagButton.backgroundTintList = context.getColorStateList(R.color.tag_color_black)
                holder.tagButton.setTextColor(ContextCompat.getColor(context, R.color.tagClickedText))
            } else {
                holder.tagButton.backgroundTintList = context.getColorStateList(R.color.tag_color)
                holder.tagButton.setTextColor(ContextCompat.getColor(context, R.color.tagNotClickedText))
                holder.tagButton.setOnClickListener { viewModel.setCurrentTag(tag) }
            }
        }
        holder.tagButton.text = tag.name
//        holder.tagButton.setOnClickListener { streamVM.setCurrentTag(tag) }
    }

    internal fun setTags(tags: List<Tag>) { updateTags(tags) }

    fun setCurrentTag(tag: Tag) {
        currentTag = tag
        notifyDataSetChanged()
    }

    private fun updateTags(t: List<Tag>) {
        val diffCallback = TagDiffCallback(this.tags, t)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.tags = t
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = tags.size
}