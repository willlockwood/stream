package willlockwood.example.stream.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.fragment_login.*
import willlockwood.example.stream.R
import willlockwood.example.stream.viewmodel.UserViewModel

class Login : Fragment() {

    lateinit var userVM: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private fun setUpViewModels() {
        userVM = ViewModelProviders.of(activity!!).get(UserViewModel::class.java)
    }

    fun getTwitterUserProfileWthTwitterCoreApi(context: Context, session: TwitterSession) {
        TwitterCore.getInstance().getApiClient(session).accountService
            .verifyCredentials(true, true, false)
            .enqueue(object : Callback<User>() {
                override fun success(result: Result<User>) {
                    val name = result.data.name
                    val userName = result.data.screenName
                    val profileImageUrl = result.data.profileImageUrl.replace("_normal", "")
                    val user = willlockwood.example.stream.model.User("twitter", name, userName, profileImageUrl)

                    userVM.setCurrentUser(user)
                    userVM.insertNewUser(user)
                    findNavController().navigate(R.id.action_login_to_streams, null, null, null)
                }

                override fun failure(exception: TwitterException) {
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun twitterSetup(context: Context) {
        twitterLoginButton.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                Log.i("success", "now")
                getTwitterUserProfileWthTwitterCoreApi(context, result.data)
            }

            override fun failure(exception: TwitterException) {
                Log.i("failure", "now")
                Toast.makeText(context,exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setUpViewModels()

        twitterSetup(context!!)

        noneLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_streams, null, null, null)
        }
    }
}
