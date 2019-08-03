package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_streams_streams.*
import willlockwood.example.stream.R
import willlockwood.example.stream.adapter.StreamListAdapter
import willlockwood.example.stream.viewmodel.StreamViewModel


class StreamsRecycler : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var viewModel: StreamViewModel
    lateinit var streamAdapter: StreamListAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_streams_streams, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModel()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView = stream_recyclerView
        streamAdapter = StreamListAdapter(this.context!!, viewModel)
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = streamAdapter

        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.smoothScrollToPosition(recyclerView.adapter!!.itemCount - 1)
            }
        }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
        viewModel.getFilteredStreams().observe(viewLifecycleOwner, Observer {
            streamAdapter.setStreams(it)
            recyclerView.scrollToPosition(streamAdapter.itemCount - 1)
        })
    }

}