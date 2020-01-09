package vnjp.monstarlaplifetime.apartmentssearch.screen.filter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.layout_filter.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepository
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepositoryImpl
import vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist.ItemsListActivity

class FilterActivity : AppCompatActivity(), View.OnClickListener {

    val isNearby = MutableLiveData<Boolean>()
    val isHighestRate = MutableLiveData<Boolean>()
    val isBestOffer = MutableLiveData<Boolean>()
    private var startPrice: Float = 0f
    private var endPrice: Float = 0f

    companion object {
        const val START_PRICE = "START_PRICE"
        const val END_PRICE = "END_PRICE"
        const val HOTEL_NAME = "HOTEL_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_filter)
        init()
        observerSearchOption()
    }

    private fun init() {
        isNearby.value = false
        isHighestRate.value = false
        isBestOffer.value = false
        textBestOffer.setOnClickListener(this)
        textHighestRate.setOnClickListener(this)
        textNearby.setOnClickListener(this)
        imageClose.setOnClickListener(this)
        textApply.setOnClickListener(this)
        setupRangeBar()
        rangeBar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            startPrice = minValue!!.toFloat()
            endPrice = maxValue!!.toFloat()
            textPriceRangeDetail.text = "$$minValue - $$maxValue"
        }
        textClear.setOnClickListener(this)
    }

    private fun setupRangeBar() {
        val roomRepository: RoomRepository =
            RoomRepositoryImpl(FirebaseDatabase.getInstance().getReference("rooms"))
        roomRepository.getPriceRange(onDataLoaded = { min, max ->
            rangeBar.setMaxValue(min)
            rangeBar.setMaxValue(max)
            textPriceRangeDetail.text = "$${min.toInt()} - $${max.toInt()}"
        }, onException = {})
    }

    override fun onClick(view: View) {
        when (view) {
            textNearby -> isNearby.value = !isNearby.value!!
            textBestOffer -> isBestOffer.value = !isBestOffer.value!!
            textHighestRate -> isHighestRate.value = !isHighestRate.value!!
            imageClose -> onBackPressed()
            textApply -> {
                val intent = Intent(this, ItemsListActivity::class.java)
                if (searchView.text.isNotEmpty()) intent.putExtra(
                    HOTEL_NAME,
                    searchView.text.toString()
                )
                intent.putExtra(START_PRICE, startPrice)
                intent.putExtra(END_PRICE, endPrice)
                startActivity(intent)
            }
            textClear -> {
                Log.d("seekbar", "on click")
            }
        }
    }

    private fun observerSearchOption() {
        isNearby.observe(this, Observer {
            Log.d("filter", "in observer")
            textNearby.setBackgroundResource(
                if (it) R.drawable.bg_nearby_selected
                else R.drawable.bg_nearby
            )
        })
        isBestOffer.observe(this, Observer {
            textBestOffer.setBackgroundResource(
                if (it) R.drawable.bg_best_offer_selected
                else R.drawable.bg_best_offer
            )
        })
        isHighestRate.observe(this, Observer {
            textHighestRate.setBackgroundResource(
                if (it) R.drawable.bg_highest_rate_selected
                else R.drawable.bg_hightes_rate
            )
        })

    }
}
