package vnjp.monstarlaplifetime.apartmentssearch.data.repository

import vnjp.monstarlaplifetime.apartmentssearch.data.model.Rent
import vnjp.monstarlaplifetime.apartmentssearch.data.model.User

interface UserRepository {

    fun getUsers(
        id: Int,
        onDataLoaded: ((List<User>) -> Unit),
        onException: ((String) -> Unit)
    )

    fun login(
        email: String,
        password: String,
        onResponseLogin: (isComplete: Boolean, message: String) -> Unit
    )

    fun register(
        email: String,
        password: String,
        user: User,
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
        rent: Rent,
        onAddResponse: (isSuccess: Boolean, message: String) -> Unit
    )

    fun deleteRent(
        userKey: String,
        rentKey: String,
        onDeleteRentResponse: (isSuccess: Boolean, message: String) -> Unit
    )


}