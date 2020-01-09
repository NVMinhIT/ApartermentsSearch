package vnjp.monstarlaplifetime.apartmentssearch.screen.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepository
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepositoryImpl

class FilterViewModel : ViewModel() {

    val isNearby = MutableLiveData<Boolean>()
    val isHighestRate = MutableLiveData<Boolean>()
    val isBestOffer = MutableLiveData<Boolean>()
    private val roomRepository: RoomRepository =
        RoomRepositoryImpl(FirebaseDatabase.getInstance().getReference("rooms"))
    private var _startPrice = MutableLiveData<Float>()
    val startPrice: LiveData<Float> = _startPrice

    private var _endPrice = MutableLiveData<Float>()
    val endPrice: LiveData<Float> = _endPrice

    private var _exception = MutableLiveData<String>()
    val exception: LiveData<String> = _exception

    var rangePrice = MutableLiveData<String>()

    fun getPriceRange() {

        roomRepository.getPriceRange(onDataLoaded = { min, max ->
            _endPrice.value = max
            _startPrice.value = min
            Log.d("filter", "on get price in range \$${min.toInt()} - \$${max.toInt()}")
            rangePrice.value = "$${min.toInt()} - $${max.toInt()}"
            Log.d("filter", "on get price in range $rangePrice")
        }, onException = {
            _exception.value = it
        })
    }
}