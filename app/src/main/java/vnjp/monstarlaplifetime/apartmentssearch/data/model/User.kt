package vnjp.monstarlaplifetime.apartmentssearch.data.model

import com.google.firebase.database.PropertyName

data class User(
    var email: String? = null,
    var name: String? = null,
    var phone_num: String? = null,
    @PropertyName("rents")
    var rents: ArrayList<Rent>? = null
)