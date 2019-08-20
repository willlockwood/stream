package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_streams_tags3.*
import willlockwood.example.stream.R
import willlockwood.example.stream.adapter.deleteTagAdapter
import willlockwood.example.stream.viewmodel.StreamViewModel

class deleteTagRecycler : Fragment() {

    lateinit var streamVM: StreamViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var deleteTagAdapter: deleteTagAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_streams_tags3, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModels()

        setUpRecyclerView()

        observeTags()

        add_button.setOnClickListener {
            streamVM.insertNewTag()
            val bundle = bundleOf("addNewTag" to true)
            findNavController().navigate(R.id.action_streams_to_tagEdit, bundle, null, null)
        }
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
    }

    private fun setUpRecyclerView() {
        recyclerView = tag_recycler
        deleteTagAdapter = deleteTagAdapter(this.context!!, streamVM)
        recyclerView.adapter = deleteTagAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeTags() {
        streamVM.getAllTags().observe(viewLifecycleOwner, Observer { deleteTagAdapter.setTags(it) })
        streamVM.getCurrentTag().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                deleteTagAdapter.setCurrentTag(it)
            }
        })
    }
}