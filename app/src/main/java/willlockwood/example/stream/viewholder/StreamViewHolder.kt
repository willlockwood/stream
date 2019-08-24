package willlockwood.example.stream.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_stream.view.*
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.TextToSpeechVM

class StreamViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    fun bindView(
        stream: Stream,
        streamVM: StreamViewModel,
        textToSpeechVM: TextToSpeechVM
    ){
        itemView.text.text = stream.text

        if (stream.threadable) {
            itemView.startThreadButton.visibility = View.VISIBLE
        } else {
            itemView.startThreadButton.visibility = View.GONE
        }

        itemView.startThreadButton.setOnClickListener {
            streamVM.newThreadFromStream(stream)
        }

//        itemView.setOnLongClickListener {
//            textToSpeechVM.setTextToSpeak(stream.text)
//            true
//        }
    }
}