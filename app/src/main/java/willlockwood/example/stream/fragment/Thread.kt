package willlockwood.example.stream.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_thread.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import willlockwood.example.stream.R
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.model.Thread
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.TextToSpeechVM
import willlockwood.example.stream.viewmodel.UserViewModel

class Thread : Fragment() {

    lateinit var streamVM: StreamViewModel
    lateinit var userVM: UserViewModel
    lateinit var textToSpeechVM: TextToSpeechVM
    lateinit var thread: Thread
    var threadStreams: List<Stream> = emptyList()

    var numberOfStreams: Int? = null

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

        setUpHeader()

        observeThreadStreams()

        setUpTextToSpeechButton()
    }

    private fun setUpTextToSpeechButton() {
        threadSpeakButton.setOnClickListener {
            var textList = emptyList<String>()
            for (stream in threadStreams) {
                textList = textList.plus(stream.text)
                textToSpeechVM.setTextToSpeak(textList)
            }
        }


    }

    private fun setUpHeader() {
        thread = streamVM.getCurrentThread().value!!

        thread_title_et.setText(thread.title)
        thread_title_txt.setText(thread.title)
        thread_title_et.isEnabled = false
        thread_title_et.visibility = View.GONE
        thread_title_done_btn.visibility = View.GONE

        thread_title_edit_btn.setOnClickListener {
            switchToEditMode()
        }

        thread_title_done_btn.setOnClickListener {
            val editTextTitle = thread_title_et.text.toString()
            if (thread.title != editTextTitle) {
                thread_title_txt.setText(editTextTitle)
                thread.title = editTextTitle
                streamVM.updateThread(thread)
            }
            switchToDisplayMode()
        }

        threadDone.setOnClickListener {
            GlobalScope.launch {
                streamVM.setCurrentThread(null)
            }
        }
    }

    private fun switchToEditMode() {
        thread_title_et.isEnabled = true
        thread_title_et.visibility = View.VISIBLE
        thread_title_txt.visibility = View.GONE
        thread_title_done_btn.visibility = View.VISIBLE
        thread_title_edit_btn.visibility = View.GONE
        threadSize.visibility = View.GONE

        thread_title_et.requestFocus()
        val imm: InputMethodManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun switchToDisplayMode() {
        thread_title_et.isEnabled = false
        thread_title_et.visibility = View.GONE
        thread_title_txt.visibility = View.VISIBLE
        thread_title_done_btn.visibility = View.GONE
        thread_title_edit_btn.visibility = View.VISIBLE
        threadSize.visibility = View.VISIBLE
    }

    private fun observeThreadStreams() {
        streamVM.getThreadStreams().observe(viewLifecycleOwner, Observer {
            threadStreams = it
            numberOfStreams = it.size
            threadSize.setText(numberOfStreams.toString())
            thread.size = it.size
            streamVM.updateThread(thread)
        })
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(activity!!).get(StreamViewModel::class.java)
        userVM = ViewModelProviders.of(activity!!).get(UserViewModel::class.java)
        textToSpeechVM = ViewModelProviders.of(activity!!).get(TextToSpeechVM::class.java)
    }

    private fun observeCurrentThread() {
        streamVM.getCurrentThread().observe(viewLifecycleOwner, Observer {
            if (it == null) {
                findNavController().navigate(R.id.action_thread_to_streams)
            } else {
                Log.i("thread", it.toString())
                // TODO
            }
        })
    }

}
