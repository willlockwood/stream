package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_fullscreen_photos_pager.*
import willlockwood.example.stream.R
import willlockwood.example.stream.adapter.FullscreenPhotosPagerAdapter

class FullscreenPhotosPager : Fragment() {

    private lateinit var uriList: ArrayList<String>
    private var startPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            startPosition = it.getInt("position", 0)
            uriList = it.getStringArrayList("uris")!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fullscreen_photos_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = viewPager
        val viewPagerAdapter = FullscreenPhotosPagerAdapter(childFragmentManager, uriList)
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = startPosition!!
    }
}
