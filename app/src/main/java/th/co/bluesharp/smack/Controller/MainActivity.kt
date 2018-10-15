package th.co.bluesharp.smack.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import th.co.bluesharp.smack.Model.Channel
import th.co.bluesharp.smack.R
import th.co.bluesharp.smack.Services.AuthService
import th.co.bluesharp.smack.Services.MessageService
import th.co.bluesharp.smack.Services.UserDataService
import th.co.bluesharp.smack.Utils.BOARDCAST_USER_DATA_CHANGE
import th.co.bluesharp.smack.Utils.SOCKET_URL

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    var selectedChannel: Channel? = null

    lateinit var channelAdapter: ArrayAdapter<Channel>

    private fun setupAdapter() {
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BOARDCAST_USER_DATA_CHANGE))
        socket.connect()
        socket.on("channelCreated", onNewChannel)
        setupAdapter()

        if (App.pref.isLoggedIn) {
            AuthService.findUser(this) {}
        }

        channel_list.setOnItemClickListener { _, _, i, _ ->
            selectedChannel = MessageService.channels[i]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()
        }

    }

    override fun onResume() {
//        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(BOARDCAST_USER_DATA_CHANGE))
        super.onResume()

    }

    private val onNewChannel = Emitter.Listener { args ->
        runOnUiThread {
            val channelName = args[0] as String
            val channelDesc = args[1] as String
            val channelId = args[2] as String
            val channel = Channel(channelName, channelDesc, channelId)
            MessageService.channels.add(channel)
            channelAdapter.notifyDataSetChanged()
//            println(channelName)
//            println(channelDesc)
//            println(channelId)
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        socket.disconnect()
        super.onDestroy()
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (App.pref.isLoggedIn) {
//                println("login ${UserDataService.name}")
                userNameNavHeader.text = UserDataService.name
//                userNameNavHeader.text = "test Something"
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userImageNavHeader.setImageResource(resourceId)
                loginBtnNavHeader.text = "LOGOUT"
                userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)

                MessageService.getChannel() { complete ->
                    if (complete) {
                        if (MessageService.channels.count() > 0) {
                            selectedChannel = MessageService.channels[0]
                            channelAdapter.notifyDataSetChanged()
                            updateWithChannel()
                        }

                    }

                }
            }

        }

    }

    fun updateWithChannel() {
        mainChannelName.text = "#${selectedChannel?.name}"
        ""
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavClicked(view: View) {
        if (App.pref.isLoggedIn) {
            UserDataService.logout()
            userNameNavHeader.text = "Login"
            userEmailNavHeader.text = ""
            loginBtnNavHeader.text = "Login"
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

    }

    fun addBtnNavClicked(view: View) {
        if (App.pref.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialog = layoutInflater.inflate(R.layout.add_channel_diag, null)
            builder.setView(dialog)
                    .setPositiveButton("Add") { dialogInterface, i ->
                        val nameTextField = dialog.findViewById<EditText>(R.id.addChannelName)
                        val descTextField = dialog.findViewById<EditText>(R.id.addChannelDescription)
                        hideKeyboard()
                        socket.emit("newChannel", nameTextField.text.toString(), descTextField.text.toString())

                    }.setNegativeButton("Cancel") { dialogInterface, i ->
                        hideKeyboard()
                    }.show()
        }
    }

    fun sendMsgBtnClicked(view: View) {

    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

    }


}
