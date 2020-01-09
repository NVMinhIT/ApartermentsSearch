package vnjp.monstarlaplifetime.apartmentssearch.utils.app

import vnjp.monstarlaplifetime.apartmentssearch.data.model.TotalGuest
import vnjp.monstarlaplifetime.apartmentssearch.utils.Sharedprf.SharedPrefsImpl

object CacheManager {

    const val ACCOUNT_PEOPLE = "ACCOUNT_PEOPLE"
    const val ACCOUNT_DAY = "ACCOUNT_DAY"
    const val OBJECT_TOTAL_GUEST = "OBJECT_TOTAL_GUEST"
    const val DATE_CHECKIN = "DATE_CHECKIN"
    const val DATE_CHECKOUT = "DATE_CHECKOUT"

    private var sharedPrefs: SharedPrefsImpl? = null

    var cacheManager: CacheManager? = null

    fun getInstance(): CacheManager? {
        return cacheManager
    }

    fun cacheNumberPeople(name: String?) {
        sharedPrefs!!.put(
            ACCOUNT_PEOPLE, name)
    }

    fun getNumberPeople(): String? {
        return sharedPrefs!!.get(
            ACCOUNT_PEOPLE, String::class.java)
    }

    fun cacheAccountDay(string: String) {
        sharedPrefs!!.put(
            ACCOUNT_DAY, string)
    }

    fun getAccountDay(): String? {
        return sharedPrefs!!.get(
            ACCOUNT_DAY, String::class.java)
    }

    fun cacheObjectTotalGuest(total: TotalGuest) {
        sharedPrefs!!.putObject(
            OBJECT_TOTAL_GUEST, total)
    }

    fun getTotalGuest(): TotalGuest {
        return sharedPrefs?.getObject(
            OBJECT_TOTAL_GUEST, TotalGuest::class.java)!!
    }

    fun cacheCheckInDate(sCheckIn: String) {
        sharedPrefs!!.put(
            DATE_CHECKIN, sCheckIn)

    }

    fun getCheckInDate(): String {
        return sharedPrefs!!.get(
            DATE_CHECKIN, String::class.java)!!

    }

    fun cacheCheckOutDate(sCheckOut: String) {
        sharedPrefs!!.put(
            DATE_CHECKOUT, sCheckOut)

    }

    fun getCheckOutDate(): String {
        return sharedPrefs!!.get(
            DATE_CHECKOUT, String::class.java)!!

    }

    operator fun invoke(sharedPrefsImpl: SharedPrefsImpl) {
        sharedPrefs = sharedPrefsImpl
        cacheManager = this
    }

}