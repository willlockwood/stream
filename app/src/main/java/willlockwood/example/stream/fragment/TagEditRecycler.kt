package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.delete_fragment_streams_tags3.tag_recycler
import kotlinx.android.synthetic.main.fragment_tag_edit.*
import willlockwood.example.stream.R
import willlockwood.example.stream.SwipeToDeleteCallback
import willlockwood.example.stream.adapter.TagEditAdapter
import willlockwood.example.stream.viewmodel.StreamViewModel

class TagEditRecycler : Fragment() {

    lateinit var streamVM: StreamViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var tagEditAdapter: TagEditAdapter
//    var

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_tag_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModels()
        setUpRecyclerView()
        observeTags()
        setUpHeaderButtons()
    }

    private fun setUpHeaderButtons() {
        add_tag_btn.setOnClickListener {
            streamVM.insertNewTag()
        }

        delete_tag_btn.setOnClickListener {
            val tags = tagEditAdapter.getTags()
            streamVM.updateTags(tags)
            streamVM.setCurrentTag(tags[0])

            findNavController().navigate(R.id.action_tagEdit_to_streams, null, null, null)
        }
    }

    private fun observeTags() {
        streamVM.getAllTags().observe(viewLifecycleOwner, Observer {
            tagEditAdapter.setTags(it)
        })
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)

        streamVM.getAllTags().observe(viewLifecycleOwner, Observer {
            tagEditAdapter.setTags(it)
        })
    }

    private fun setUpRecyclerView() {
        recyclerView = tag_recycler
        tagEditAdapter = TagEditAdapter(this.context!!, streamVM)
        recyclerView.adapter = tagEditAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Swipe To Delete
        val swipeDeleteHandler = object : SwipeToDeleteCallback(context!!, "tags") {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                tagEditAdapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(recyclerView)

    }

}