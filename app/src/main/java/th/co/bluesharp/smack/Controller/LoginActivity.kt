package th.co.bluesharp.smack.Controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import th.co.bluesharp.smack.CreateUserActivity
import th.co.bluesharp.smack.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginCreateBtnClicked(view:View){
        val createIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createIntent)
    }

    fun loginBtnClicked(view: View){

    }

}
