package vnjp.monstarlaplifetime.apartmentssearch.data

import java.text.SimpleDateFormat
import java.util.*

object Commons {

    private var DATE_FORMAT_T: String = "EEE, MMM dd"
    private var K_DATE_FORMAT: String = "MMM dd"


    fun getStringCurrentDateTime(date: Date): String? {
        val formatter = SimpleDateFormat(
            DATE_FORMAT_T,
            Locale.US
        )
        return formatter.format(date)
    }

    fun getCurrentDateTime(dateCurrent: Date): String? {
        val formatter = SimpleDateFormat(
            K_DATE_FORMAT,
            Locale.US
        )
        return formatter.format(dateCurrent)
    }
}