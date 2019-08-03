package willlockwood.example.stream

import android.net.Uri
import androidx.room.TypeConverter
import com.github.salomonbrys.kotson.typeToken
import com.google.gson.Gson

class UriConverters {
//    @TypeConverter
//    fun fromString(value: String?): Uri? {
//        return if (value == null) null else Uri.parse(value)
//    }

    @TypeConverter
    fun toString(uri: Uri?): String? {
        return uri.toString()
    }
//
    @TypeConverter
    fun fromString(value: String?): List<Uri>? {
        if (value != null) {
            val gson = Gson()
            val type = typeToken<List<Uri>>()
            return gson.fromJson(value, type)
        }
        return null
    }
//
//    @TypeConverter
//    fun toString(uris: List<String>?): String? {
//        return if (uris != null) {
//            val gson = Gson()
//            val type = typeToken<List<String>>()
//            val jsonString = gson.toJson(uris, type)
//            Log.i("converter", uris.toString())
//            Log.i("converter", jsonString)
//            jsonString
//        } else { null }
//    }

//    @TypeConverter
//    fun saveList(uris: String): String? {
//        return if (uris != null) {
//            val gson = Gson()
//            val type = typeToken<List<String>>()
//            val jsonString = gson.toJson(uris, type)
//            Log.i("converter", uris.toString())
//            Log.i("converter", jsonString)
//            jsonString
//        } else { null }
//    }

//    @RequiresApi(Build.VERSION_CODES.P)
//    @TypeConverter
//    fun toString(uris: List<Uri>?): String? {
//        return if (uris != null) {
//            val gson = Gson()
//    //            val type = typeToken<List<Uri>>().typeName
//            val type = typeToken<List<Uri>>()
//            val jsonString = gson.toJson(uris, type)
//            Log.i("converter", uris.toString())
//            Log.i("converter", jsonString)
//            jsonString
//        } else { null }
//    }

}