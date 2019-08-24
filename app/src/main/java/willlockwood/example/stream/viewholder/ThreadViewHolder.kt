package willlockwood.example.stream.viewholder

import android.util.Log
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
import willlockwood.example.stream.viewmodel.TextToSpeechVM

class ThreadViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    fun bindView(
        stream: Stream,
        streamVM: StreamViewModel,
        textToSpeechVM: TextToSpeechVM
    ){

        var thread: Thread?
        var streams: List<Stream>?

        suspend fun getThread() {
            thread = streamVM.getThreadById(stream.thread!!)

            withContext(Dispatchers.Main) {
                itemView.cardText.text = thread!!.title
                itemView.threadSize.text = thread!!.size.toString()
            }
        }

        suspend fun getStreams() {
            streams = streamVM.returnStreamsByThreadId(stream.thread!!)
            Log.i("streams", streams.toString())

            withContext(Dispatchers.Main) {
                var streamTexts : List<String> = emptyList()
                for (s in streams!!) {
                    Log.i("s", s.toString())
                    streamTexts = streamTexts.plus(s.text)
//                    streamTexts.plus(s.text)
                }

//                itemView.setOnLongClickListener {
//                    textToSpeechVM.setTextToSpeak(streamTexts)
//                    true
//                }
            }
        }

//        suspend fun getThread() {
//            thread = streamVM.getThreadById(stream.thread!!)
//
//            withContext(Dispatchers.Main) {
//                itemView.cardText.text = thread!!.title
//                itemView.threadSize.text = thread!!.size.toString()
//            }
//        }

        itemView.cardText.text = stream.text

        GlobalScope.launch(Dispatchers.Main) {
            getThread()
            getStreams()
        }

//        itemView.cardStack.setOnLongClickListener {
//
//        }

        itemView.cardStack.setOnClickListener {
            GlobalScope.launch {
                streamVM.setCurrentThread(stream.thread)
            }
//            streamVM.setThreadBeingEdited(stream.thread)
        }
    }
}