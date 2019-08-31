package willlockwood.example.stream

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import willlockwood.example.stream.viewmodel.SpeechRecognizerViewModel
import willlockwood.example.stream.viewmodel.StreamViewModel
import willlockwood.example.stream.viewmodel.TextToSpeechVM
import willlockwood.example.stream.viewmodel.UserViewModel
import java.util.*

class MainActivity : AppCompatActivity(),
    TextToSpeech.OnInitListener,
    NavigationView.OnNavigationItemSelectedListener
{

    private lateinit var streamVM: StreamViewModel
    private lateinit var userVM: UserViewModel
    private lateinit var speechVM: SpeechRecognizerViewModel
    private lateinit var textToSpeechVM: TextToSpeechVM
    private var textToSpeech: TextToSpeech? = null

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setUpNavigation()


        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
//        // Hide keyboard
//        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        setUpViewModels()

        userVM.getTwitterUser().observe(this, Observer {
            if (it == null) {
                Log.i("mainActivity", "null")
            } else {
                Log.i("mainActivity", it.toString())
            }
        })

        textToSpeech = TextToSpeech(this, this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            observeTextToSpeech()
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawer_layout.closeDrawers()

        val id = item.itemId
        when (id) {
            R.id.nav_menu_home -> navController.navigate(R.id.streams)
            R.id.nav_menu_tags -> navController.navigate(R.id.login_fragment)
            R.id.nav_menu_log_in -> navController.navigate(R.id.login_fragment)
            R.id.nav_menu_privacy -> navController.navigate(R.id.aboutTerms)
            R.id.nav_menu_terms -> navController.navigate(R.id.aboutPrivacy)
        }

        return true
    }

    private fun setUpNavigation() {
        setSupportActionBar(toolbar as Toolbar)

        navController = (nav_host_fragment as NavHostFragment).navController

        NavigationUI.setupWithNavController(navigationView, navController)

        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setUpViewModels() {
        streamVM = ViewModelProviders.of(this).get(StreamViewModel::class.java)
        userVM = ViewModelProviders.of(this).get(UserViewModel::class.java)
        speechVM = ViewModelProviders.of(this).get(SpeechRecognizerViewModel::class.java)
        textToSpeechVM = ViewModelProviders.of(this).get(TextToSpeechVM::class.java)
    }

    private fun observeTextToSpeech() {
        textToSpeechVM.getTextToSpeak().observe(this, Observer {
            for ((i, text) in it.withIndex()) {
                val queueMode: Int = when (i) {
                    0 -> TextToSpeech.QUEUE_FLUSH
                    else -> TextToSpeech.QUEUE_ADD
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    textToSpeech!!.speak(text, queueMode, null, "")
                }
            }
        })
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported")
            } else {
                // THIS IS WHERE THE UI WOULD BE UPDATED
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // TODO: This is a baaad way to do this (indexing the fragments) so fix this at some point
        val loginFragment = nav_host_fragment.childFragmentManager.fragments[0]
        loginFragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}