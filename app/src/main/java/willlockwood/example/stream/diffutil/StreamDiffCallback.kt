package willlockwood.example.stream.diffutil

import androidx.recyclerview.widget.DiffUtil
import willlockwood.example.stream.model.Stream

class StreamDiffCallback(private val oldList: List<Stream>, private val newList: List<Stream>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].streamId === newList[newItemPosition].streamId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldStream = oldList[oldItemPosition]
        val newStream = newList[newItemPosition]

        return oldStream == newStream
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }


}