package vnjp.monstarlaplifetime.apartmentssearch.data.repository

import vnjp.monstarlaplifetime.apartmentssearch.data.model.Rent

interface UserRepository {

    fun login(
        email: String,
        password: String,
        onResponseLogin: (isComplete: Boolean, message: String) -> Unit
    )

    fun register(
        email: String,
        password: String,
        onResponseRegister: (isComplete: Boolean, message: String) -> Unit
    )

    fun updateRent(
        userKey: String,
        rentKey: String,
        rent: Rent,
        onUpdateResponse: (isSuccess: Boolean, message: String) -> Unit
    )

    fun addRent(
        userKey: String,
        rentKey: String,
        rent: Rent,
        onAddResponse: (isSuccess: Boolean, message: String) -> Unit
    )

    fun deleteRent(
        userKey: String,
        rentKey: String,
        onDeleteRentResponse: (isSuccess: Boolean, message: String) -> Unit
    )


}