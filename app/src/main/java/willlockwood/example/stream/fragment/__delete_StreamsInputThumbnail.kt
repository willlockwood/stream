package willlockwood.example.stream.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import willlockwood.example.stream.R

class __delete_StreamsInputThumbnail : Fragment() {

    private lateinit var imgUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imgUri = it.getParcelable<Uri>("imgUri")!!
        }
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(imgUri: Uri) =
//            __delete_StreamsInputThumbnail().apply {
//                arguments = Bundle().apply {
//                    putParcelable("imgUri", imgUri)
//                }
//            }
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.row_input_thumbnail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imageView = (activity as AppCompatActivity).findViewById<ImageView>(R.id.imageView)



        imageView.setImageDrawable(null)
        imageView.setImageURI(imgUri)
    }
}
