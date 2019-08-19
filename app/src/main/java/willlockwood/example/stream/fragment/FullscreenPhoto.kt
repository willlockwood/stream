package willlockwood.example.stream.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.page_fullscreen_photo.*
import willlockwood.example.stream.R

class FullscreenPhoto : Fragment() {

    private lateinit var uri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { uri = it.getString("uri")!! }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.page_fullscreen_photo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val imageView = fullscreen_photo_image

//        Glide.with(context!!)
//            .load(File(uri.path))
//            .into(imageView)

        imageView.setImageURI(Uri.parse(uri))
    }

    companion object {
        @JvmStatic
        fun newInstance(uri: String) =
            FullscreenPhoto().apply {
                arguments = Bundle().apply {
                    putString("uri", uri)
                }
            }
    }
}