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

//        var threadLD = MutableLiveData<Thread>()
//
////        threadLD.observe(this, Observer {
////
////        })
//        threadLD.observe()
//
//        streamVM.getThreadById(stream.thread)

        suspend fun getThread() {
            thread = streamVM.getThreadById(stream.thread!!)
//            Log.i("thread", thread.toString())
//            if (thread!!.title != null) {
//
            withContext(Dispatchers.Main) {
                if (thread!!.title != null) {
                    itemView.cardText.text = thread!!.title
                }
            }
//            uiThread {
//
//            }
//
//                itemView.cardText.text = thread!!.title
//            }
////            itemView.cardText = stream.text
        }

//        val thread = streamVM.getThreadById(stream.thread!!)

//        itemView.cardText.text = thread!!.title

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