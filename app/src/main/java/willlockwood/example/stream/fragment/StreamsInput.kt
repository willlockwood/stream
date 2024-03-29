package willlockwood.example.stream.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_streams_input.*
import willlockwood.example.stream.R
import willlockwood.example.stream.adapter.InputThumbnailsAdapter
import willlockwood.example.stream.model.Stream
import willlockwood.example.stream.viewmodel.SpeechRecognizerViewModel
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.UserViewModel
import java.io.File

class StreamsInput : Fragment() {

    lateinit var streamVM: StreamViewModel
    lateinit var userVM: UserViewModel
    lateinit var speechVM: SpeechRecognizerViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var thumbnailAdapter: InputThumbnailsAdapter


    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
        private val REQUEST_RECORD_AUDIO_PERMISSION = 1200
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_streams_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModels()

        speechVM.getViewState().observe(viewLifecycleOwner, Observer<SpeechRecognizerViewModel.ViewState>{ viewState ->
            render(viewState)
        })
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(activity!!)[StreamViewModel::class.java]
        userVM = ViewModelProviders.of(activity!!)[UserViewModel::class.java]
        speechVM = ViewModelProviders.of(activity!!)[SpeechRecognizerViewModel::class.java]
    }

    private fun setUpRecyclerView() {
        recyclerView = images
        thumbnailAdapter = InputThumbnailsAdapter(this.context!!, streamVM)
        recyclerView.adapter = thumbnailAdapter
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        thumbnailAdapter.setOnItemClickListener(object : InputThumbnailsAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, uris: List<String>, uri: String) {
                streamVM.deleteThumbnailUris(uri)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpRecyclerView()

        observeCurrentTag()

//        setUpThreadWarningBar()
        observeIfOnThread()

        observeThumbnailUris()

        setUpInputButtons()


    }

    private fun observeIfOnThread() {
        streamVM.getCurrentThread().observe(viewLifecycleOwner, Observer {
            when (it) {
                null -> streamInputEditText.setHint("What are you thinking?")
                else -> streamInputEditText.setHint("Add to this thread...")
            }
        })
    }

    // Disappears the input bar when on an un-modifiable tag (currently, only the "About" tag)
    private fun observeCurrentTag() {
        streamVM.getCurrentTag().observe(viewLifecycleOwner, Observer {
            when (it.name) {
                "About" -> this.view!!.visibility = View.GONE
                else -> this.view!!.visibility = View.VISIBLE
            }
        })
    }

    // Updates the thumbnail bar UI
    private fun observeThumbnailUris() {
        streamVM.getThumbnailUris().observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                images.visibility = View.GONE
                thumbnailAdapter.setUris(emptyList())
            } else {
                images.visibility = View.VISIBLE
                thumbnailAdapter.setUris(it.toList())
            }
        })
    }

    private fun render(uiOutput: SpeechRecognizerViewModel.ViewState?) {
        if (uiOutput == null) return
        streamInputEditText.setText(uiOutput.spokenText)
        Log.i("isListening", speechVM.isListening.toString())
        if (!speechVM.isListening) {

            if (uiOutput.spokenText.endsWith(" end stream")) {
                streamInputEditText.setText(uiOutput.spokenText.dropLast(11))
                speechVM.resetViewState()
                streamUploadButton.performClick()
                streamInputEditText.setText("")

            } else if (uiOutput.spokenText.endsWith(" start new stream")) {
                streamInputEditText.setText(uiOutput.spokenText.dropLast(17))
                speechVM.resetViewState()
                streamUploadButton.performClick()
                streamInputEditText.setText("")
                speechButton.performClick()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val micClickListener = View.OnClickListener {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (activity!!.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO) //permission denied
                requestPermissions(permissions, REQUEST_RECORD_AUDIO_PERMISSION) //show popup to request runtime permission
            } else if (speechVM.isListening) { speechVM.stopListening()
            } else { speechVM.startListening() }
        } else if (speechVM.isListening) { speechVM.stopListening()
        } else { speechVM.startListening() } //system OS is < Marshmallow
    }

    private val streamUploadClickListener = View.OnClickListener {
        val streamText = streamInputEditText.editableText.toString()

        if (streamIsReadyToUpload(streamText)) {

            val newStream = Stream(streamVM.getCurrentTag().value!!.name,
                streamText,
                deleteable = true,
                tweetable = true,
                threadable = true,
                thread = streamVM.getCurrentThread().value?.id
            )

            if (streamVM.getCurrentThread().value != null) {
                newStream.threadable = false
                newStream.positionInThread = streamVM.getThreadStreams().value!!.size
            }

            newStream.imageUris = streamVM.getThumbnailUris().value?.joinToString(", ")
            streamVM.insertStream(newStream)

            speechVM.resetViewState()

            streamInputEditText.setText("")
            images.visibility = View.GONE
            streamVM.clearThumbnailUris()

            // TODO: make it so that posting a threaded stream sets the VM's streamBeingThreaded to the new stream
//            if (streamBeingThreadedId != null) {  }
        }
    }

    private val streamImageClickListener = View.OnClickListener {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (activity!!.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE) //permission denied
                requestPermissions(permissions, PERMISSION_CODE) //show popup to request runtime permission
            } else { pickImageFromGallery() }
        } else { pickImageFromGallery() } //system OS is < Marshmallow
    }

    // Sets listeners for the stream upload button and the add images button
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpInputButtons() {
        streamUploadButton.setOnClickListener(streamUploadClickListener)
        streamAddImageButton.setOnClickListener(streamImageClickListener)
        speechButton.setOnClickListener(micClickListener)
    }

    // Starts the image picker intent
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    // Updates ViewModel with image URIs in the image picker intent's result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val clipData = data?.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val file: File = getBitmapFile(clipData.getItemAt(i).uri)
                        streamVM.addThumbnailUris(file.toString())
                    }
                }
            } else {
                streamVM.addThumbnailUris(data?.data!!.toString())
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

        }
    }

    // Make bitmap file from one of the intent's clipData's items
    private fun getBitmapFile(selectedImage: Uri): File {
        val cursor = context!!.contentResolver.query(selectedImage, arrayOf(MediaStore.Images.ImageColumns.DATA), null, null, null)!!
        cursor.moveToFirst()

        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        val selectedImagePath = cursor.getString(idx)
        cursor.close()

        return File(selectedImagePath)

    }

    // Handles the result of asking for media access permissions
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_RECORD_AUDIO_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speechButton.performClick()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Logic for whether the stream the user is attempting to upload is a valid stream
    private fun streamIsReadyToUpload(text: String): Boolean {
        return text != ""
    }


}