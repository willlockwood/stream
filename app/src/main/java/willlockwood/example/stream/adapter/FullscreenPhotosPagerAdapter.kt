package willlockwood.example.stream.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import willlockwood.example.stream.fragment.FullscreenPhoto

class FullscreenPhotosPagerAdapter(
    fragmentManager: FragmentManager,
    private val uriList: ArrayList<String>
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return FullscreenPhoto.newInstance(uriList[position])
    }

    override fun getCount(): Int {
        return uriList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

}