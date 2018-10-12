package th.co.bluesharp.smack

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {


    var userAvartar = "profiledefault"
    var avatarColor = "[0.5,0.5,0.5,1]"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
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
