package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import willlockwood.example.stream.R
import willlockwood.example.stream.viewmodel.NavigationVM
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.UserViewModel

class Streams : Fragment() {

    lateinit var streamVM: StreamViewModel
    lateinit var userVM: UserViewModel
    lateinit var navigationVM: NavigationVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpViewModels()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCurrentThread()
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
        userVM = ViewModelProviders.of(activity!!).get(UserViewModel::class.java)
        navigationVM = ViewModelProviders.of(activity!!).get(NavigationVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_streams, container, false)
    }

    private fun observeCurrentThread() {
        streamVM.getCurrentThread().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                // TODO
            } else {
                findNavController().navigate(R.id.action_streams_to_thread)
            }
        })
    }
}
