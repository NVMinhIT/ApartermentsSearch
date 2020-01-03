package vnjp.monstarlaplifetime.apartmentssearch.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Query(
    val hotelName: String,
    val isNearby: Boolean,
    val isHighestRate: Boolean,
    val isBestOffer: Boolean,
    val startPrice: Float,
    val endPrice: Float,
    val isEntirePlace: Boolean,
    val isPrivateRoom: Boolean,
    val isHotelRoom: Boolean,
    val isShareRoom: Boolean
) : Parcelable

