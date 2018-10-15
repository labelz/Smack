package th.co.bluesharp.smack.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException
import th.co.bluesharp.smack.Controller.App
import th.co.bluesharp.smack.Model.Channel
import th.co.bluesharp.smack.Model.Message
import th.co.bluesharp.smack.Utils.URL_GET_CHANNEL
import th.co.bluesharp.smack.Utils.URL_GET_MESSAGES

object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannel(complete: (Boolean) -> Unit) {
        val request = object : JsonArrayRequest(Method.GET, URL_GET_CHANNEL, null, Response.Listener { response ->
            try {
//                channels = ArrayList<Channel>()

                for (x in 0 until response.length()) {
                    val c = response.getJSONObject(x)
                    var channel = Channel(c.getString("name"), c.getString("description"), c.getString("_id"))
                    this.channels.add(channel)
                }
                println("channel size: " + this.channels)
                complete(true)
            } catch (e: JSONException) {
                Log.d("ERROR", "EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not get channel:${error}")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }


            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header.put("Authorization", "Bearer ${App.pref.authToken}")
                return header
            }
        }

        App.pref.requestQueue.add(request)
    }

    fun getMessage(channelId: String, complete: (Boolean) -> Unit) {
        messages.clear()
        val request = object : JsonArrayRequest(Method.GET, "$URL_GET_MESSAGES${channelId}", null, Response.Listener { response ->
            try {
                for (x in 0 until response.length()) {
                    val c = response.getJSONObject(x)
                    var msg = Message(c.getString("messageBody"), c.getString("userName"), c.getString("channelId")
                            , c.getString("userAvatar"), c.getString("userAvatarColor"), c.getString("_id"), c.getString("timeStamp"))
                    this.messages.add(msg)
                }
                complete(true)
            } catch (e: JSONException) {
                Log.d("ERROR", "EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not get message:${error}")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }


            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header.put("Authorization", "Bearer ${App.pref.authToken}")
                return header
            }
        }
        App.pref.requestQueue.add(request)
    }

    fun clearMessage() {
        messages.clear()
    }

    fun clearChannel() {
        channels.clear()
    }
}