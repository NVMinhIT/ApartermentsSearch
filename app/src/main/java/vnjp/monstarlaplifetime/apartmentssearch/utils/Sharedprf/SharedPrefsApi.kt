package vnjp.monstarlaplifetime.apartmentssearch.utils.Sharedprf

interface SharedPrefsApi {
    operator fun <T> get(key: String?, clazz: Class<T>?): T?
    fun <T> put(key: String?, data: T)
    fun clear()
}
