package vnjp.monstarlaplifetime.apartmentssearch.screen.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepository
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepositoryImpl

class AuthenticationViewModel : ViewModel() {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")
    private var userRepository: UserRepository = UserRepositoryImpl(databaseReference, firebaseAuth)

    private var _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _exception = MutableLiveData<String>()
    val exception: LiveData<String> = _exception

    fun login(email: String, password: String) {
        userRepository.login(
            email,
            password,
            onResponseLogin = { isComplete, message ->
                if (isComplete) _isSuccess.value = isComplete
                else _exception.value = message
            })
    }

    fun register(email: String, password: String) {
        userRepository.register(email, password, onResponseRegister = { isComplete, message ->
            if (isComplete) {
                _isSuccess.value = isComplete
            } else {
                _exception.value = message
            }
        })
    }


}