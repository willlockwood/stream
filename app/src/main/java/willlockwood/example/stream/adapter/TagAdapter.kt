package willlockwood.example.stream.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.BR
import willlockwood.example.stream.MainActivity
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

    private lateinit var binding: ViewDataBinding

    inner class TagViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(tag: Tag) {
            binding.setVariable(BR.tag, tag)
            binding.setVariable(BR.viewmodel, viewModel)
            binding.lifecycleOwner = (context as MainActivity)
//            viewModel.getCurrentTag().observe((context as MainActivity), Observer {
//                if (it != null) {
//                    Log.i(tag.name, (it == tag).toString())
//                    binding.setVariable(BR.isChecked, it == tag)
//                    binding.executePendingBindings()
//                }

//                if (it == tag) {
////                    binding.setVariable(BR.isChecked, it == tag)
//                    Log.i("tag", tag.toString())
//                } else {
//                    Log.i("notTag", it.toString())
//                    Log.i("notTag", tag.toString())
//                }
//            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        binding = DataBindingUtil.inflate(inflater, R.layout.row_tag, parent, false)
        return TagViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val tag = tags[position]
        holder.bindView(tag)

//        val currentTag = viewModel.getCurrentTag().value
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (tag == viewModel.getCurrentTag().value) {
//                holder.tagButton.backgroundTintList = context.getColorStateList(R.color.tag_color_black)
//                holder.tagButton.setTextColor(ContextCompat.getColor(context, R.color.tagClickedText))
//            } else {
//                holder.tagButton.backgroundTintList = context.getColorStateList(R.color.tag_color)
//                holder.tagButton.setTextColor(ContextCompat.getColor(context, R.color.tagNotClickedText))
//                holder.tagButton.setOnClickListener { viewModel.setCurrentTag(tag) }
//            }
//        }
//        holder.tagButton.text = tag.name
//        holder.tagButton.setOnClickListener { streamVM.setCurrentTag(tag) }
    }

    internal fun setTags(tags: List<Tag>) { updateTags(tags) }

//    fun setCurrentTag(tag: Tag) {
//        currentTag = tag
//        notifyDataSetChanged()
//    }

    private fun updateTags(t: List<Tag>) {
        val diffCallback = TagDiffCallback(this.tags, t)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.tags = t
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = tags.size
}