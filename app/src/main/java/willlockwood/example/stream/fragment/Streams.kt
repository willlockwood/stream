package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import willlockwood.example.stream.R
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.TwitterViewModel

class Streams : Fragment() {

    lateinit var streamViewModel: StreamViewModel
    lateinit var userVM: TwitterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpViewModel()
    }
    private fun setUpViewModel() {
        streamViewModel = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
        userVM = ViewModelProviders.of(activity!!).get(TwitterViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_streams, container, false)
    }
}
