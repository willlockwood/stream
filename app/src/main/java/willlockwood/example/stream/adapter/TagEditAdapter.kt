package willlockwood.example.stream.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.R
import willlockwood.example.stream.diffutil.TagEditDiffCallback
import willlockwood.example.stream.model.Tag
import willlockwood.example.stream.viewholder.TagEditViewHolder
import willlockwood.example.stream.viewmodel.StreamViewModel

class TagEditAdapter internal constructor(
    private val context: Context,
    private var streamVM: StreamViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tags = emptyList<Tag>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CellType.EDITABLE_TAG.ordinal -> TagEditViewHolder(inflater.inflate(R.layout.row_tag_edit, parent, false))
            else -> TagEditViewHolder(inflater.inflate(R.layout.row_tag_edit, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            CellType.EDITABLE_TAG.ordinal -> {
                val editTagViewHolder = holder as TagEditViewHolder
                editTagViewHolder.bindView(tags[position], streamVM, context)
            }
            CellType.NON_EDITABLE_TAG.ordinal -> {
                val editTagViewHolder = holder as TagEditViewHolder
                editTagViewHolder.bindView(tags[position], streamVM, context)
            }
        }
    }

    enum class CellType(viewType: Int) {
        EDITABLE_TAG(0),
        NON_EDITABLE_TAG(1)
    }

    internal fun isTagDeleteableAtPosition(position: Int): Boolean {
        val tag = this.tags[position]
        return tag.deleteable
    }

    internal fun setTags(tags: List<Tag>) { updateTags(tags) }

    private fun updateTags(tags: List<Tag>) {
        val diffCallback = TagEditDiffCallback(this.tags, tags)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.tags = tags
        diffResult.dispatchUpdatesTo(this)
    }

    internal fun removeAt(index: Int) { streamVM.deleteTag(this.tags[index]) }

    fun getTags(): List<Tag> {
        return this.tags
    }

    override fun getItemCount() = tags.size
}