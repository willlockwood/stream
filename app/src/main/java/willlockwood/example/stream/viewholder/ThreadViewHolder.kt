package willlockwood.example.stream.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_stream_stack.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.model.Thread
import willlockwood.example.stream.viewmodel.StreamViewModel

class ThreadViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    fun bindView(stream: Stream, streamVM: StreamViewModel){

        var thread: Thread?

        suspend fun getThread() {
            thread = streamVM.getThreadById(stream.thread!!)

            withContext(Dispatchers.Main) {
                itemView.cardText.text = thread!!.title
                itemView.threadSize.text = thread!!.size.toString()
            }
        }

        itemView.cardText.text = stream.text

        GlobalScope.launch(Dispatchers.Main) {
            getThread()
        }

        itemView.cardStack.setOnClickListener {
            GlobalScope.launch {
                streamVM.setCurrentThread(stream.thread)
            }
//            streamVM.setThreadBeingEdited(stream.thread)
        }
    }
}