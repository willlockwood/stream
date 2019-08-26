package willlockwood.example.stream.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_streams_tags.*
import willlockwood.example.stream.R
import willlockwood.example.stream.viewmodel.StreamViewModel

class Tags : Fragment() {

    lateinit var streamVM: StreamViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(willlockwood.example.stream.R.layout.fragment_streams_tags, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModels()

        observeTags()

        setUpChipGroup()

        add_tag_button.setOnClickListener {
            streamVM.insertNewTag()
            val bundle = bundleOf("addNewTag" to true)
            findNavController().navigate(willlockwood.example.stream.R.id.action_streams_to_tagEdit, bundle, null, null)
        }
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
    }

    private fun setUpChipGroup() {
        tagsGroup.setOnCheckedChangeListener { chipGroup, checkedChipId ->
            for (chip in chipGroup.children) {
                if ((chip as Chip).id == checkedChipId) {
                    chip.isClickable = false
                    chip.setChipBackgroundColorResource(R.color.tag_color_black)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        chip.setTextColor(context!!.getColorStateList(R.color.tag_text_color_white))
                    }
                } else {
                    chip.isClickable = true
                    chip.isChecked = false
                    chip.setChipBackgroundColorResource(R.color.tag_color)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        chip.setTextColor(context!!.getColorStateList(R.color.tag_color_black))
                    }
                }
            }
        }
    }

    private fun observeTags() {
        streamVM.getAllTags().observe(viewLifecycleOwner, Observer {

            var currentChip: Chip? = null
            tagsGroup.clearCheck()
            tagsGroup.removeAllViews()
            if (it != null) {
                for (tag in it) {
                    val chipToAdd = Chip(context)

                    chipToAdd.id = tag.id

                    chipToAdd.isCheckable = true
                    chipToAdd.isCheckedIconVisible = false
                    chipToAdd.text = tag.name
                    chipToAdd.setOnClickListener {
                        streamVM.setCurrentTag(tag)
                    }
                    tagsGroup.addView(chipToAdd)

                    if (chipToAdd.text == streamVM.getCurrentTag().value?.name) {
                        currentChip = chipToAdd
                    }
                }
                if (currentChip != null) {
                    if (tagsGroup.checkedChipId < 0) {
                        tagsGroup.check(currentChip.id)
                    }
                }
            }
        })
        streamVM.getCurrentTag().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                tagsGroup.check(it.id)
            }
        })
    }


}