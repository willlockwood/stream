package willlockwood.example.stream.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import willlockwood.example.stream.R

class Splash : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val stream = (activity as AppCompatActivity).findViewById<ImageButton>(R.id.stream)

        stream.setOnClickListener{
//            findNavController().navigate(R.id.action_splash_to_login, null, null, null)
            findNavController().navigate(R.id.action_splash_to_streams, null, null, null)
        }
//        findNavController().navigate(R.id.action_splash_to_streams, null, null, null)
    }
}
