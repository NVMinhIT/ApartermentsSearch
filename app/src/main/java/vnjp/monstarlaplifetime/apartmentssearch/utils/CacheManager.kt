package vnjp.monstarlaplifetime.apartmentssearch.utils

import vnjp.monstarlaplifetime.apartmentssearch.utils.Sharedprf.SharedPrefsImpl

object CacheManager {

    const val ACCOUNT_PEOPLE = "ACCOUNT_PEOPLE"

    private var sharedPrefs: SharedPrefsImpl? = null

    var cacheManager: CacheManager? = null

    fun getInstance(): CacheManager? {
        return cacheManager
    }

    fun cacheNumberPeople(name: String?) {
        sharedPrefs!!.put(ACCOUNT_PEOPLE, name)
    }

    fun getNumberPeople(): String? {
        return sharedPrefs!!.get(ACCOUNT_PEOPLE, String::class.java)
    }

    operator fun invoke(sharedPrefsImpl: SharedPrefsImpl) {
        this.sharedPrefs = sharedPrefsImpl
        cacheManager = this
    }

}