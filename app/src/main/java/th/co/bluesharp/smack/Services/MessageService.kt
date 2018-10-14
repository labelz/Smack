package th.co.bluesharp.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import th.co.bluesharp.smack.Model.Channel
import th.co.bluesharp.smack.Utils.URL_GET_CHANNEL

object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannel(context: Context, complete: (Boolean) -> Unit) {
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
                header.put("Authorization", "Bearer ${AuthService.authToken}")
                return header
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}