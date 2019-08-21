package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_thread.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import willlockwood.example.stream.R
import willlockwood.example.stream.model.Thread
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.UserViewModel

class Thread : Fragment() {

    lateinit var streamVM: StreamViewModel
    lateinit var userVM: UserViewModel
    lateinit var thread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thread, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModels()

        observeCurrentThread()
//        observeThreadBeingEdited()

//        GlobalScope.launch {
            setUpHeader()
//        }

        setUpDoneButton()
    }

    private fun setUpHeader() {
        thread = streamVM.getCurrentThread().value!!

        if (thread.title != null) {
            threadTitle.setText(thread.title)
        }

        threadTitle.setOnFocusChangeListener { _, b ->
            if (!b) {
                thread.title = threadTitle.text.toString()
                streamVM.updateThread(thread)
            }
        }

    }

    private fun setUpDoneButton() {
        threadDone.setOnClickListener {
            GlobalScope.launch {
                streamVM.setCurrentThread(null)
            }
//            streamVM.setThreadBeingEdited(null)
        }
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
        userVM = ViewModelProviders.of(activity!!).get(UserViewModel::class.java)
    }

    private fun observeThreadBeingEdited() {
        streamVM.getThreadBeingEdited().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().navigate(R.id.action_thread_to_streams)
            } else {
                // TODO
            }
        })
    }

    private fun observeCurrentThread() {
        streamVM.getCurrentThread().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().navigate(R.id.action_thread_to_streams)
            } else {
//                threadHeaderEditText
                // TODO
            }
        })
    }

}
