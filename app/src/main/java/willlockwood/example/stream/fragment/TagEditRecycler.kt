package willlockwood.example.stream.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.delete_fragment_streams_tags3.tag_recycler
import kotlinx.android.synthetic.main.fragment_tag_edit.*
import willlockwood.example.stream.R
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

        val addButton: ImageButton = add_tag_button_2
        val doneButton: ImageButton = done_button

        addButton.setOnClickListener {
            streamVM.insertNewTag()
        }

        doneButton.setOnClickListener {
            val tags = tagEditAdapter.getTags()
            streamVM.updateTags(tags)
            Log.i("blah", "blah")
            Log.i("blah", "blah")
            Log.i("blah", "blah")
            streamVM.setCurrentTag(tags[0])

            findNavController().navigate(R.id.action_tagEdit_to_streams, null, null, null)
        }
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
    }

}