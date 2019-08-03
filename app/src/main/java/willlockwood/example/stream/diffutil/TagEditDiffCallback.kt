package willlockwood.example.stream.diffutil

import androidx.recyclerview.widget.DiffUtil
import willlockwood.example.stream.model.Tag

class TagEditDiffCallback(private val oldList: List<Tag>, private val newList: List<Tag>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTag = oldList[oldItemPosition]
        val newTag = newList[newItemPosition]

        return oldTag.id == newTag.id
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }


}