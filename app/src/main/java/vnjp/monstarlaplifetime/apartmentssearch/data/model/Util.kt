package vnjp.monstarlaplifetime.apartmentssearch.data.model

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import vnjp.monstarlaplifetime.apartmentssearch.R
import java.text.DecimalFormat
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class Util {
    companion object {
        fun getStatisticRooms() = Room(
            Address(
                "28 Great Sutton St, Clerkenwell, London, EC1V 0DS",
                21.0124,
                105.8992
            ),
            listOf(
                "TV",
                "Washer",
                "Coffee maker",
                "TV",
                "Washer",
                "Wifi"
            ),
            "This reservation is non-refundable because check-in is less than 7 days away.",
            hashMapOf(),
            "Slumber on a plush Eve memory foam mattress\u200B in the courtyard-facing bedroom of this characterful modernised period apartment. There's classy wooden flooring throughout while the traditional fireplace lends a cosy focal point to the living room.",
            1,
            "https://images.oyoroomscdn.com/uploads/hotel_image/81612/large/f0af88ee7aca453a.jpg",
            "Cozy Victorian Apartment in Islington",
            null,
            120f,
            arrayListOf(
                "No pets",
                "No smoking, parties, or events",
                "Check-in is anytime after 3PM and check out by 11AM"
            )
        )

        fun calculationByDistance(StartP: LatLng, EndP: LatLng): Double {
            val radius = 6371 // radius of earth in Km
            val lat1 = StartP.latitude
            val lat2 = EndP.latitude
            val lon1 = StartP.longitude
            val lon2 = EndP.longitude
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = (sin(dLat / 2) * sin(dLat / 2)
                    + (cos(Math.toRadians(lat1))
                    * cos(Math.toRadians(lat2)) * sin(dLon / 2)
                    * sin(dLon / 2)))
            val c = 2 * asin(sqrt(a))
            val valueResult = radius * c
            val km = valueResult / 1
            val newFormat = DecimalFormat("####")
            val kmInDec: Int = Integer.valueOf(newFormat.format(km))
            val meter = valueResult % 1000
            val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
            return radius * c
        }
    }
}

class Type {
    companion object {
        val amenitiesImage = mapOf(
            "Wifi" to R.drawable.ic_feather_wifi,
            "Coffee maker" to R.drawable.ic_feather_coffee,
            "TV" to R.drawable.ic_feather_monitor,
            "Washer" to R.drawable.ic_feather_speaker
        )
    }
}

internal suspend fun <T> awaitTaskResult(task: Task<T>): T = suspendCoroutine { continuation ->
    task.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            continuation.resume(task.result!!)
        } else {
            continuation.resumeWithException(task.exception!!)
        }
    }
}

//Wraps Firebase/GMS calls
internal suspend fun <T> awaitTaskCompletable(task: Task<T>): Unit =
    suspendCoroutine { continuation ->
        task.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resume(Unit)
            } else {
                continuation.resumeWithException(task.exception!!)
            }
        }
    }