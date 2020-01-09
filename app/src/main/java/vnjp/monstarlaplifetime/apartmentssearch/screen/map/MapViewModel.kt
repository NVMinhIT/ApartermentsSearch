package vnjp.monstarlaplifetime.apartmentssearch.screen.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepositoryImpl

class MapViewModel : ViewModel() {

    private val roomDatabaseReference = FirebaseDatabase.getInstance().getReference("rooms")
    private val roomRepository = RoomRepositoryImpl(roomDatabaseReference)

    private var _rooms = MutableLiveData<HashMap<String, Room>>()
    val rooms: LiveData<HashMap<String, Room>> = _rooms

    private var _room = MutableLiveData<Room>()
    val room: LiveData<Room> = _room

    private var _exception = MutableLiveData<String>()
    val exception: LiveData<String> = _exception

    fun getRoomInRange(current: LatLng, range: Double) {
        roomRepository.getRoomInRange(current,
            10.toDouble(),
            onDataLoaded = { _rooms.value = it },
            onException = { _exception.value = it })
    }

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
}