package vnjp.monstarlaplifetime.apartmentssearch.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Rent
import vnjp.monstarlaplifetime.apartmentssearch.data.model.User

class UserRepositoryImpl(
    userDatabaseReference: DatabaseReference,
    firebaseAuth: FirebaseAuth
) : UserRepository {

    override fun getUsers(
        id: Int,
        onDataLoaded: (List<User>) -> Unit,
        onException: (String) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun login(
        email: String,
        password: String,
        onResponseLogin: (isComplete: Boolean, message: String) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun register(
        email: String,
        password: String,
        user: User,
        onResponseRegister: (isComplete: Boolean, message: String) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRent(
        userKey: String,
        rentKey: String,
        rent: Rent,
        onUpdateResponse: (isSuccess: Boolean, message: String) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addRent(
        userKey: String,
        rent: Rent,
        onAddResponse: (isSuccess: Boolean, message: String) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteRent(
        userKey: String,
        rentKey: String,
        onDeleteRentResponse: (isSuccess: Boolean, message: String) -> Unit
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}