package willlockwood.example.stream.diffutil

import androidx.recyclerview.widget.DiffUtil

class InputThumbnailsDiffCallback(private val oldList: List<String>, private val newList: List<String>) : DiffUtil.Callback() {

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
        val oldUri = oldList[oldItemPosition]
        val newUri = newList[newItemPosition]

        return oldUri == newUri
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }


}