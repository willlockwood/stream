package willlockwood.example.stream.viewholder

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_tag_edit.view.*
import willlockwood.example.stream.model.Tag
import willlockwood.example.stream.viewmodel.StreamViewModel

class TagEditViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

    fun disappear(element: View) {
        element.visibility = View.GONE
    }
    fun reappear(element: View) {
        element.visibility = View.VISIBLE
    }

    fun bindView(
        tag: Tag,
        streamVM: StreamViewModel,
        context: Context
    ){
        var tagText = tag.name
        itemView.display_tag_txt.setText(tagText)
        itemView.edit_tag_et.setText(tagText)

        val editText = itemView.edit_tag_et
        val displayText = itemView.display_tag_txt
        val finishedEditingButton = itemView.finish_tag_btn
        val startEditingButton = itemView.edit_tag_btn

        finishedEditingButton.setOnClickListener {
            disappear(finishedEditingButton)
            disappear(editText)

            reappear(startEditingButton)
            reappear(displayText)

            val newText = editText.text.toString()
            displayText.setText(newText)
            tag.name = newText

            streamVM.updateTag(tag)

            reappear(startEditingButton)
            reappear(displayText)
        }

        startEditingButton.setOnClickListener {
            disappear(startEditingButton)
            disappear(displayText)

            reappear(finishedEditingButton)
            reappear(editText)

            editText.requestFocus()
            val imm: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }

//        if (tag.moveable) { tag.position = position }
//
//        holder.editText.setText(tag.name)
//
//        holder.editText.isEnabled = tag.renameable
//
//        if (tag.deleteable) {
//            holder.deleteButton.visibility = View.VISIBLE
//            holder.deleteButton.setOnClickListener {
//                streamVM.deleteTag(tag)
//            }
//        } else {
//            holder.deleteButton.visibility = View.INVISIBLE
//        }
//
    }
}