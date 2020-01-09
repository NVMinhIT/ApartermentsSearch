package vnjp.monstarlaplifetime.apartmentssearch.screen.detailroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Rent
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepositoryImpl
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepositoryImpl

class DetailRoomViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userRepository =
        UserRepositoryImpl(FirebaseDatabase.getInstance().getReference("users"), firebaseAuth)
    val roomRepository = RoomRepositoryImpl(FirebaseDatabase.getInstance().getReference("rooms"))

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _room = MutableLiveData<Room>()
    val room: LiveData<Room> = _room

    private var _exception = MutableLiveData<String>()
    val exception: LiveData<String> = _exception

    private var _isAddRentSuccess = MutableLiveData<Boolean>()
    val isAddRentSuccess: LiveData<Boolean> = _isAddRentSuccess

    fun getRoom(key: String) {
        roomRepository.getDetailRoom(
            key,
            onDataLoaded = {
                _room.value = it
            },
            onException = {
                _exception.value = it
            }
        )
    }

    fun addRent(
        userKey: String,
        idRoom: String,
        rent: Rent
    ) {
        userRepository.addRent(
            userKey,
            idRoom,
            rent,
            onAddResponse = { isSuccess, message ->
                if (isSuccess) _isAddRentSuccess.value = isSuccess
                else _exception.value = message
            })
    }
}