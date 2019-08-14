package willlockwood.example.stream.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import willlockwood.example.stream.model.StreamUser

object Helper {

//    fun startShareActivity(context: Context, user: StreamUser) {
//        val activity = context as Activity
//        val intent = Intent(context, MainActivity::class.java)
////        val intent = Intent(context, login)
////        val intent = object : Intent()
//        intent.putExtra("user", user)
//        activity.startActivityForResult(intent, 3)
////        activity.finish()
//    }

    fun getTwitterUserProfileWthTwitterCoreApi(
        context: Context, session: TwitterSession
    ) {
        TwitterCore.getInstance().getApiClient(session).accountService
            .verifyCredentials(true, true, false)
            .enqueue(object : Callback<User>() {
                override fun success(result: Result<User>) {
                    Log.i("helper function", "now")
                    val name = result.data.name
                    val userName = result.data.screenName
                    val profileImageUrl = result.data.profileImageUrl.replace("_normal", "")
                    val user = StreamUser(name, userName, profileImageUrl, "twitter")
//                    startShareActivity(context, user)
                }

                override fun failure(exception: TwitterException) {
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
    }

}