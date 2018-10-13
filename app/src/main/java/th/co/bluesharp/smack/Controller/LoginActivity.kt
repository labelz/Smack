package th.co.bluesharp.smack.Controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import th.co.bluesharp.smack.R
import th.co.bluesharp.smack.Services.AuthService

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginCreateBtnClicked(view: View) {
        val createIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createIntent)
        finish()
    }

    fun loginBtnClicked(view: View) {

        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        AuthService.loginUser(this, "j@c.com", "123456") { complete ->
            if (complete) {

            }
        }
    }

}
