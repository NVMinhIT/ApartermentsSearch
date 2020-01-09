package vnjp.monstarlaplifetime.apartmentssearch.screen.filter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.layout_filter.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist.ItemsListActivity

class FilterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var filterViewModel: FilterViewModel
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
        filterViewModel = ViewModelProviders.of(this).get(FilterViewModel::class.java)
        filterViewModel.isNearby.value = false
        filterViewModel.isHighestRate.value = false
        filterViewModel.isBestOffer.value = false
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
        filterViewModel.getPriceRange()
        filterViewModel.rangePrice.observe(this, Observer {
            textPriceRangeDetail.text = it
        })
    }

    override fun onClick(view: View) {
        when (view) {
            textNearby -> filterViewModel.isNearby.value = !filterViewModel.isNearby.value!!
            textBestOffer -> filterViewModel.isBestOffer.value =
                !filterViewModel.isBestOffer.value!!
            textHighestRate -> filterViewModel.isHighestRate.value =
                !filterViewModel.isHighestRate.value!!
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
        filterViewModel.isNearby.observe(this, Observer {
            Log.d("filter", "in observer")
            textNearby.setBackgroundResource(
                if (it) R.drawable.bg_nearby_selected
                else R.drawable.bg_nearby
            )
        })
        filterViewModel.isBestOffer.observe(this, Observer {
            textBestOffer.setBackgroundResource(
                if (it) R.drawable.bg_best_offer_selected
                else R.drawable.bg_best_offer
            )
        })
        filterViewModel.isHighestRate.observe(this, Observer {
            textHighestRate.setBackgroundResource(
                if (it) R.drawable.bg_highest_rate_selected
                else R.drawable.bg_hightes_rate
            )
        })
        filterViewModel.startPrice.observe(this, Observer {
            rangeBar.setMinValue(it)
        })

        filterViewModel.endPrice.observe(this, Observer {
            rangeBar.setMaxValue(it)
        })
        filterViewModel.exception.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }
}
