package th.co.bluesharp.smack.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import org.json.JSONException
import org.json.JSONObject
import th.co.bluesharp.smack.Controller.App
import th.co.bluesharp.smack.Utils.*

object AuthService {


    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()
//        println(requestBody)

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            //            println(response)
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user:${error}")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.pref.requestQueue.add(registerRequest)

    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()

        val request2 = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
            //            println(response)
            try {
                App.pref.authToken = response.getString("token")
                App.pref.userEmail = response.getString("user")
                App.pref.isLoggedIn = true
                complete(true)
            } catch (e: JSONException) {
                Log.d("ERROR", "EXC: ${e.localizedMessage}")
                complete(false)
            }


        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not login user:${error}")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.pref.requestQueue.add(request2)
    }

    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("name", name)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)

        val requestBody = jsonBody.toString()

        val request3 = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->
            //                        println(response)
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.id = response.getString("_id")
                complete(true)
            } catch (e: JSONException) {
                Log.d("ERROR", "EXC: ${e.localizedMessage}")
                complete(false)
            }

//            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not create user:${error}")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val header = HashMap<String, String>()
                header.put("Authorization", "Bearer ${App.pref.authToken}")
                return header
            }
        }

        App.pref.requestQueue.add(request3)

    }

    fun findUser(context: Context, complete: (Boolean) -> Unit) {
        val request = object : JsonObjectRequest(Method.GET, "${URL_FIND_USER}${App.pref.userEmail}", null, Response.Listener { response ->
            //                        println(response)
            try {
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.id = response.getString("_id")
                val userDataChange = Intent(BOARDCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            } catch (e: JSONException) {
                Log.d("ERROR", "EXC: ${e.localizedMessage}")
                complete(false)
            }

//            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not create user:${error}")
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
}