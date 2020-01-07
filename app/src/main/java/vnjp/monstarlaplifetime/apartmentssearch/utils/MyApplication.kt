package vnjp.monstarlaplifetime.apartmentssearch.utils

import android.app.Application
import vnjp.monstarlaplifetime.apartmentssearch.utils.Sharedprf.SharedPrefsImpl

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        myApplication = this
        CacheManager(SharedPrefsImpl(this))
    }

    companion object {
        var myApplication: MyApplication? = null

    }

    fun getInstance(): MyApplication? {
        return myApplication
    }
}