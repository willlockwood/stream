package willlockwood.example.stream.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.R
import willlockwood.example.stream.diffutil.TagEditDiffCallback
import willlockwood.example.stream.model.Tag
import willlockwood.example.stream.viewmodel.StreamViewModel


class TagEditAdapter internal constructor(
    context: Context,
    private var viewModel: StreamViewModel
    ) : RecyclerView.Adapter<TagEditAdapter.TagViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var tags = emptyList<Tag>()

    inner class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val editText: EditText = itemView.findViewById(R.id.editText)
//        val deleteButton: Button = itemView.findViewById(R.id.tag_delete_button)
        val deleteButton: ImageButton = itemView.findViewById(R.id.done_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val itemView = inflater.inflate(R.layout.row_tag_edit, parent, false)
        return TagViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]

        if (tag.moveable) { tag.position = position }

        holder.editText.setText(tag.name)

        holder.editText.isEnabled = tag.renameable

        if (tag.deleteable) {
            holder.deleteButton.visibility = View.VISIBLE
            holder.deleteButton.setOnClickListener {
                viewModel.deleteTag(tag)
            }
        } else {
            holder.deleteButton.visibility = View.INVISIBLE
        }

        holder.editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(cs: CharSequence, s: Int, b: Int, c: Int) { tag.name = cs.toString() }
            override fun afterTextChanged(editable: Editable) {}
            override fun beforeTextChanged(cs: CharSequence, i: Int, j: Int, k: Int) {}
        })
    }

    internal fun setTags(tags: List<Tag>) { updateTags(tags) }

    private fun updateTags(tags: List<Tag>) {
        val diffCallback = TagEditDiffCallback(this.tags, tags)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.tags = tags
        diffResult.dispatchUpdatesTo(this)
    }

    fun getTags(): List<Tag> {
        return this.tags
    }

    override fun getItemCount() = tags.size

}