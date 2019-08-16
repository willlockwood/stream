package willlockwood.example.stream

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import willlockwood.example.stream.viewmodel.SpeechRecognizerViewModel
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var streamVM: StreamViewModel
    private lateinit var userVM: UserViewModel
    private lateinit var speechVM: SpeechRecognizerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModels()

        userVM.getTwitterUser().observe(this, Observer {
            if (it == null) {
                Log.i("mainActivity", "null")
            } else {
                Log.i("mainActivity", it.toString())
            }
        })
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(this).get(StreamViewModel::class.java)
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
        speechVM = ViewModelProviders.of(this).get(SpeechRecognizerViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO: This is a baaad way to do this (indexing the fragments) so fix this at some point
        val loginFragment = nav_host_fragment.childFragmentManager.fragments[0]
        loginFragment?.onActivityResult(requestCode, resultCode, data)
    }
}