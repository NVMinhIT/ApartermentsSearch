package vnjp.monstarlaplifetime.apartmentssearch.screen.filter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stfalcon.pricerangebar.model.BarEntry
import kotlinx.android.synthetic.main.layout_filter.*
import vnjp.monstarlaplifetime.apartmentssearch.R

class FilterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_filter)

        val barEntrys = ArrayList<BarEntry>()
        barEntrys.add(BarEntry(30.0f, 5.0f))
        barEntrys.add(BarEntry(32.0f, 7.0f))
        barEntrys.add(BarEntry(34.0f, 10.0f))
        barEntrys.add(BarEntry(36.0f, 11.0f))
        barEntrys.add(BarEntry(38.0f, 14.0f))
        barEntrys.add(BarEntry(40.0f, 15.0f))
        seekBar.setEntries(barEntrys)
    }
}
