package willlockwood.example.stream.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_stream.view.*
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewmodel.StreamViewModel

class StreamInThreadViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    fun bindView(stream: Stream, streamVM: StreamViewModel){
        itemView.text.text = stream.text
    }
}