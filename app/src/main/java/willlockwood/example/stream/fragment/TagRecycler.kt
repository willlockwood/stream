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
import kotlinx.android.synthetic.main.fragment_streams_tags.*
import willlockwood.example.stream.R
import willlockwood.example.stream.adapter.TagAdapter
import willlockwood.example.stream.viewmodel.StreamViewModel

class TagRecycler : Fragment() {

    lateinit var viewModel: StreamViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var tagAdapter: TagAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_streams_tags, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        add_button.setOnClickListener {
            viewModel.insertNewTag()
            val bundle = bundleOf("addNewTag" to true)
            findNavController().navigate(R.id.action_streams_to_tagEdit, bundle, null, null)
        }

        setUpViewModel()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        recyclerView = tag_recycler
        tagAdapter = TagAdapter(this.context!!, viewModel)
        recyclerView.adapter = tagAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)

        viewModel.getAllTags().observe(viewLifecycleOwner, Observer { tagAdapter.setTags(it) })

        viewModel.getCurrentTag().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                tagAdapter.setCurrentTag(it)
            }
        })
    }


}