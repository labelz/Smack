package th.co.bluesharp.smack.Controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import th.co.bluesharp.smack.R
import th.co.bluesharp.smack.Services.AuthService
import th.co.bluesharp.smack.Utils.BOARDCAST_USER_DATA_CHANGE

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginCreateBtnClicked(view: View) {
        val createIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createIntent)
        finish()
    }

    fun loginBtnClicked(view: View) {
        hideKeyboard()
        enableSpinner(true)
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        AuthService.loginUser(this, email, password) { complete ->
            if (complete) {
                AuthService.findUser(this) { foundUser ->
                    if (foundUser) {
                        val userDataChange = Intent(BOARDCAST_USER_DATA_CHANGE)
                        LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                        finish()
                        enableSpinner(false)
                    } else {
                        errorToast()
                    }
                }
            } else {
                errorToast()
            }
        }
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginBtn.isEnabled = !enable
        loginCreateBtn.isEnabled = !enable
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        enableSpinner(false)
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

    }

}
