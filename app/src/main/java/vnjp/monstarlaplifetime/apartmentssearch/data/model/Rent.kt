package vnjp.monstarlaplifetime.apartmentssearch.data.model

data class Rent(
    var checkin_date: String? = null,
    var checkout_date: String? = null,
    var room_id: Int? = null,
    var total_guest: TotalGuest? = null
)