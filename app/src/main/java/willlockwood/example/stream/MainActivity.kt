package willlockwood.example.stream

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
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
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(this).get(StreamViewModel::class.java)
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
        speechVM = ViewModelProviders.of(this).get(SpeechRecognizerViewModel::class.java)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 140 && resultCode == -1) {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_login_to_streams, null, null, null)
        }
    }
}