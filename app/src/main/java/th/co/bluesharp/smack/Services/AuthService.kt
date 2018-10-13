package th.co.bluesharp.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import th.co.bluesharp.smack.Utils.URL_CREATE_USER
import th.co.bluesharp.smack.Utils.URL_LOGIN
import th.co.bluesharp.smack.Utils.URL_REGISTER

object AuthService {

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()
        println(requestBody)

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            println(response)
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

        Volley.newRequestQueue(context).add(registerRequest)

    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()

        val request = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
            //            println(response)
            try {
                authToken = response.getString("token")
                userEmail = response.getString("user")
                isLoggedIn = true
            } catch (e: JSONException) {
                Log.d("ERROR", "EXC: ${e.localizedMessage}")
                complete(false)
            }

            complete(true)
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

        Volley.newRequestQueue(context).add(request)
    }

    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {
        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("name", name)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)

        val requestBody = jsonBody.toString()

        val request = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener { response ->
            //            println(response)
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

            complete(true)
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
                var header = HashMap<String, String>()
                header.put("Authorization", "Bearer ${authToken}")
                return header
            }
        }

        Volley.newRequestQueue(context).add(request)

    }
}