package vnjp.monstarlaplifetime.apartmentssearch.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Rent

class UserRepositoryImpl(
    private val userDatabaseReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : UserRepository {

    override fun login(
        email: String,
        password: String,
        onResponseLogin: (isComplete: Boolean, message: String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResponseLogin.invoke(it.isSuccessful, "Login successfully")
                } else {
                    it.exception?.message?.let { mess ->
                        onResponseLogin.invoke(
                            it.isSuccessful,
                            mess
                        )
                    }
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        onResponseRegister: (isComplete: Boolean, message: String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onResponseRegister.invoke(it.isSuccessful, "Register successfully")
                } else {
                    it.exception?.message?.let { mess ->
                        onResponseRegister.invoke(
                            it.isSuccessful,
                            mess
                        )
                    }
                }
            }
    }

    override fun updateRent(
        userKey: String,
        rentKey: String,
        rent: Rent,
        onUpdateResponse: (isSuccess: Boolean, message: String) -> Unit
    ) {
        var message: String?
        var result: Boolean
        userDatabaseReference
            .child(userKey)
            .child("rents")
            .child(rentKey)
            .setValue(rent)
            .addOnSuccessListener {
                message = "Remove successfully"
                result = true
                onUpdateResponse.invoke(result, message!!)
            }.addOnFailureListener {
                message = it.message
                result = false
                onUpdateResponse.invoke(result, message!!)
            }
    }

    override fun addRent(
        userKey: String,
        roomKey: String,
        rent: Rent,
        onAddResponse: (isSuccess: Boolean, message: String) -> Unit
    ) {
        var message: String?
        var result: Boolean

        userDatabaseReference.child(userKey)
            .child(roomKey)
            .setValue(rent)
            .addOnSuccessListener {
                message = "add successfully"
                result = true
                onAddResponse.invoke(result, message!!)
            }.addOnFailureListener {
                message = it.message
                result = false
                onAddResponse.invoke(result, message!!)
            }
    }

    override fun deleteRent(
        userKey: String,
        rentKey: String,
        onDeleteRentResponse: (isSuccess: Boolean, message: String) -> Unit
    ) {
        var message: String?
        var result: Boolean

        userDatabaseReference.child(userKey)
            .child("rents")
            .child(rentKey)
            .removeValue()
            .addOnSuccessListener {
                message = "Remove successfully"
                result = true
                onDeleteRentResponse.invoke(result, message!!)
            }.addOnFailureListener {
                message = it.message
                result = false
                onDeleteRentResponse.invoke(result, message!!)
            }
    }
}