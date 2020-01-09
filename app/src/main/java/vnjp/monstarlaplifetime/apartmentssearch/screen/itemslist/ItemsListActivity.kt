package vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_items_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room
import vnjp.monstarlaplifetime.apartmentssearch.screen.detailroom.DetailRoomActivity
import vnjp.monstarlaplifetime.apartmentssearch.screen.filter.FilterActivity
import vnjp.monstarlaplifetime.apartmentssearch.screen.guests.DateRangPickerBottomSheet
import vnjp.monstarlaplifetime.apartmentssearch.screen.guests.GuestsFragmentBottomSheet
import vnjp.monstarlaplifetime.apartmentssearch.screen.map.MapsActivity
import vnjp.monstarlaplifetime.apartmentssearch.utils.app.CacheManager


class ItemsListActivity : AppCompatActivity() {
    private lateinit var itemsListAdapter: ItemsListAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var buttonOpenMap: ImageButton
    lateinit var btCalendar: Button
    lateinit var btGuest: Button
    lateinit var btFilter: Button
    private var id: String? = null
    private var startPrice: Float = 0f
    private var endPrice: Float = 0f
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items_list)
        databaseReference = FirebaseDatabase.getInstance().getReference("rooms")
        startPrice = intent.getFloatExtra(FilterActivity.START_PRICE, 0f)
        endPrice = intent.getFloatExtra(FilterActivity.END_PRICE, 0f)
        initView()
        initEvent()
    }
    companion object {
        const val LATITUDE = "LATITUDE"
        const val LONG_TITUDE = "LONG_TITUDE"
    }
    override fun onStart() {
        super.onStart()
        itemsListAdapter.startListening()
        EventBus.getDefault().register(this)
    }
    private fun initView() {
        val query: Query = if (startPrice == endPrice) {
            databaseReference.limitToLast(50)
        } else {
            databaseReference.orderByChild("price")
                .startAt(startPrice.toDouble())
                .endAt(endPrice.toDouble())
                .limitToLast(50)
        }
        val firebaseRecyclerOptions: FirebaseRecyclerOptions<Room> =
            FirebaseRecyclerOptions.Builder<Room>().setQuery(query, Room::class.java).build()
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                val size = p0.childrenCount
                val number = "<b>${size}</b> rooms "
                tvNumberRoom.text = Html.fromHtml(number, Html.FROM_HTML_MODE_LEGACY)
            }
        })
        recyclerView = findViewById(R.id.rvListItems)
        buttonOpenMap = findViewById(R.id.imbFeather)
        btCalendar = findViewById(R.id.btCalendar)
        btGuest = findViewById(R.id.btGuest)
        btFilter = findViewById(R.id.btFilter)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemsListAdapter = ItemsListAdapter(firebaseRecyclerOptions, this)
        recyclerView.adapter = itemsListAdapter
        itemsListAdapter.onClick = { post, selectedKey ->
            id = selectedKey
            val day = CacheManager.cacheManager?.getAccountDay()
            val numberPeople = CacheManager.cacheManager?.getNumberPeople()
            if (day!!.isEmpty() && numberPeople!!.isEmpty()) {
                Toast.makeText(this, "Please Choose People !!! ", Toast.LENGTH_LONG).show()
            } else {
                val intent = Intent(this, DetailRoomActivity::class.java)
                intent.putExtra(ItemsListAdapter.BUNDLE_ID_ROOM, id)
                startActivity(intent)
            }
        }
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(sDay: String) {
        btCalendar.text = sDay

    }
    private fun initEvent() {
        btGuest.setOnClickListener {
            val bottomSheetFragment =
                GuestsFragmentBottomSheet()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            val numberPeople = StringBuffer()

            bottomSheetFragment.getGuest = {
                numberPeople.append(it).append(" guests ")
                btGuest.text = numberPeople
            }
        }
        btFilter.setOnClickListener {
            startActivity(Intent(this, FilterActivity::class.java))
        }

        buttonOpenMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
        btCalendar.setOnClickListener {
            btCalendar.isEnabled = true
            val bottomSheetFragment =
                DateRangPickerBottomSheet()
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

    }
    override fun onStop() {
        super.onStop()
        itemsListAdapter.stopListening()
        EventBus.getDefault().unregister(this)
    }


}
