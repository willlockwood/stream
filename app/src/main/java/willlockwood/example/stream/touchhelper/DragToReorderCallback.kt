package willlockwood.example.stream.touchhelper

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import willlockwood.example.stream.adapter.StreamsAdapter


//class SimpleItemTouchHelperCallback(private val mAdapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {
//
//    override fun isLongPressDragEnabled(): Boolean {
//        return true
//    }
//
//    override fun isItemViewSwipeEnabled(): Boolean {
//        return true
//    }
//
//    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
//        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
//        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
//        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
//    }
//
//    override fun onMove(
//        recyclerView: RecyclerView, viewHolder: ViewHolder,
//        target: ViewHolder
//    ): Boolean {
//        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
//        return true
//    }
//
//    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
//        mAdapter.onItemDismiss(viewHolder.adapterPosition)
//    }
//
//}

abstract class DragToReorderCallback(context: Context) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
//        val streamAdapter = recyclerView.adapter as StreamsAdapter
//
//        if (!streamAdapter.isStreamDeleteableAtPosition(viewHolder.adapterPosition)) {
//            return 0
//        }
        return super.getMovementFlags(recyclerView, viewHolder)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val streamAdapter = recyclerView.adapter as StreamsAdapter
        streamAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }
}