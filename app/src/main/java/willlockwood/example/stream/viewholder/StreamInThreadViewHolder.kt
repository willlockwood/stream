package willlockwood.example.stream.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_stream.view.text
import kotlinx.android.synthetic.main.row_stream_in_thread.view.*
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.TextToSpeechVM

class StreamInThreadViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    fun bindView(
        stream: Stream,
        streamVM: StreamViewModel,
        textToSpeechVM: TextToSpeechVM
    ){
        itemView.text.text = stream.text

        itemView.speakButton.setOnClickListener {
            textToSpeechVM.setTextToSpeak(stream.text)
        }
    }
}