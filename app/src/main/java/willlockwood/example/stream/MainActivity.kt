package willlockwood.example.stream

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import willlockwood.example.stream.viewmodel.StreamViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: StreamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModel()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProviders.of(this).get(StreamViewModel::class.java)
    }
}