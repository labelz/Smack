package th.co.bluesharp.smack.Controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_user.*
import th.co.bluesharp.smack.R
import th.co.bluesharp.smack.Services.AuthService
import th.co.bluesharp.smack.Utils.BOARDCAST_USER_DATA_CHANGE
import java.util.*

class CreateUserActivity : AppCompatActivity() {


    var userAvartar = "profiledefault"
    var avatarColor = "[0.5,0.5,0.5,1]"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        createSpinner.visibility = View.INVISIBLE
    }

    fun changeBgBtnClicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        avatarColor = "[${r.toDouble() / 255}, ${g.toDouble() / 255}, ${b.toDouble() / 255}, 1]"
        createUserImage.setBackgroundColor(Color.rgb(r, g, b))
    }

    fun createUserBtnClicked(view: View) {
        var email = createEmailText.text.toString()
        var password = createPasswordText.text.toString()
        var userName = createUsernameText.text.toString()
        enableSpinner(true)

        AuthService.registerUser(this, email, password) { registerComplete ->
            if (registerComplete) {
                AuthService.loginUser(this, email, password) { loginComplete ->
                    if (loginComplete) {
                        AuthService.createUser(this, userName, email, userAvartar, avatarColor) { createComplete ->
                            if (createComplete) {
//                                println("name ${UserDataService.name}")
//                                println("email ${UserDataService.email}")
                                enableSpinner(false)
                                val userDataChange = Intent(BOARDCAST_USER_DATA_CHANGE)
                                LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                finish()
                            } else
                                errorToast()
                        }
                    } else
                        errorToast()
                }
            } else
                errorToast()
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            createSpinner.visibility = View.VISIBLE
            createUserBtn.isEnabled = false
            createUserImage.isEnabled = false
            changeBgBtn.isEnabled = false
        } else {
            createSpinner.visibility = View.INVISIBLE
            createUserBtn.isEnabled = true
            createUserImage.isEnabled = true
            changeBgBtn.isEnabled = true
        }

    }

    fun changeImageClicked(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)
        userAvartar = when (color) {
            0 -> "light${avatar}"
            else -> "dark${avatar}"
        }

        val resorceId = resources.getIdentifier(userAvartar, "drawable", packageName)
        createUserImage.setImageResource(resorceId)

    }

}
