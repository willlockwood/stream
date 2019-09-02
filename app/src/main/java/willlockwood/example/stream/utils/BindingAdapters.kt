package willlockwood.example.stream.utils

import androidx.databinding.BindingAdapter
import com.google.android.material.chip.Chip
import willlockwood.example.stream.model.Tag

//@BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
//fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
//    progressBar.progress = (likes * max / 5).coerceAtMost(max)
//}

@BindingAdapter("app:currentTag")
fun updateCurrentTag(chip: Chip, currentTag: Tag) {
    chip.isChecked = chip.text == currentTag.name
}
//@BindingAdapter("app:progressColor")
//fun setProgressColor(view: Chip, checked: Boolean) {
//    val color = getAssociatedColor(popularity, view.context)
//    view. = ColorStateList.valueOf(color)
//}
//
//private fun getAssociatedColor(context: Context): Int {
//    return when (popularity) {
//        Popularity.NORMAL -> context.theme.obtainStyledAttributes(
//            intArrayOf(android.R.attr.colorForeground)
//        ).getColor(0, 0x000000)
//        Popularity.POPULAR -> ContextCompat.getColor(context, R.color.popular)
//        Popularity.STAR -> ContextCompat.getColor(context, R.color.star)
//    }
//}
