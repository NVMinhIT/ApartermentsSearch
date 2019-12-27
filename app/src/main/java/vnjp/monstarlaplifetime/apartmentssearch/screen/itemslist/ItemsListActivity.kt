package vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_items_list.*
import vnjp.monstarlaplifetime.apartmentssearch.MapsActivity
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room
import vnjp.monstarlaplifetime.apartmentssearch.screen.detailroom.DetailRoomActivity
import vnjp.monstarlaplifetime.apartmentssearch.screen.guests.DateRangPickerBottomSheet
import vnjp.monstarlaplifetime.apartmentssearch.screen.guests.GuestsFragmentBottomSheet


class ItemsListActivity : AppCompatActivity() {
    private lateinit var itemsListAdapter: ItemsListAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var buttonOpenMap: ImageButton
    lateinit var btCalendar: Button

    companion object {
        const val BUNDLE_ID = "BUNDLE_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_list)
        initView()
        initEvent()
        // val options = RequestOptions()
//        options.centerCrop().transform(RoundedCorners(DimensionUtil.dp2px(context, 12)))
//        Glide.with(this.applicationContext).load(R.drawable.room).apply(options).into(iv)
    }

    private fun initView() {
        recyclerView = findViewById(R.id.rvListItems)
        buttonOpenMap = findViewById(R.id.imbFeather)
        btCalendar = findViewById(R.id.btCalendar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemsListAdapter = ItemsListAdapter(this) {
            val intent = Intent(this, DetailRoomActivity::class.java)
            intent.putExtra(BUNDLE_ID, itemsListAdapter.getPosition(it).idRoom)
            startActivity(intent)
        }
        recyclerView.adapter = itemsListAdapter
        val arrayList = arrayListOf(
            Room("R1", "Sunny Soho Flat", "", 120, 4, 2),
            Room("R1", "Sunny Soho Flat", "", 120, 4, 2),
            Room("R1", "Sunny Soho Flat", "", 120, 4, 2),
            Room("R1", "Sunny Soho Flat", "", 120, 4, 2),
            Room("R1", "Sunny Soho Flat", "", 120, 4, 2),
            Room("R1", "Sunny Soho Flat", "", 120, 4, 2),
            Room("R1", "Sunny Soho Flat", "", 120, 4, 2),
            Room("R1", "Sunny Soho Flat", "", 120, 4, 2)
        )
        itemsListAdapter.setListRoom(arrayList)
    }

    private fun initEvent() {
        btGuest.setOnClickListener {
            val bottomSheetFragment =
                GuestsFragmentBottomSheet()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.getTag())
        }
        buttonOpenMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        btCalendar.setOnClickListener {
            val bottomSheetFragment =
                DateRangPickerBottomSheet()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.getTag())
        }
    }


}
