package willlockwood.example.stream.viewholder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_stream_in_thread.view.*
import willlockwood.example.stream.BR
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.TextToSpeechVM

class StreamInThreadViewHolder(
    var binding: ViewDataBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bindView(
        stream: Stream,
        streamVM: StreamViewModel,
        textToSpeechVM: TextToSpeechVM
    ){
        binding.setVariable(BR.stream, stream)

        itemView.speakButton.setOnClickListener {
            textToSpeechVM.setTextToSpeak(stream.text)
        }
    }
}