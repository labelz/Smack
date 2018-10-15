package th.co.bluesharp.smack.Services

import android.graphics.Color
import th.co.bluesharp.smack.Controller.App
import java.util.*

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun returnAvatarColor(): Int {
        val st = avatarColor.replace("[", "").replace("]", "").replace(",", "")
        var r = 0
        var g = 0
        var b = 0

        var scanner = Scanner(st)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r, g, b)
    }

    fun returnAvatarColor(avatarColor: String): Int {
        val st = avatarColor.replace("[", "").replace("]", "").replace(",", "")
        var r = 0
        var g = 0
        var b = 0

        var scanner = Scanner(st)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r, g, b)
    }

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        App.pref.authToken = ""
        App.pref.userEmail = ""
        App.pref.isLoggedIn = false
        MessageService.clearChannel()
        MessageService.clearMessage()
    }
}