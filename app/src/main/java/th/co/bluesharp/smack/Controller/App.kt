package th.co.bluesharp.smack.Controller

import android.app.Application
import th.co.bluesharp.smack.Utils.SharePref

class App : Application() {

    companion object {
        lateinit var pref: SharePref
    }

    override fun onCreate() {
        pref = SharePref(applicationContext)
        super.onCreate()
    }
}