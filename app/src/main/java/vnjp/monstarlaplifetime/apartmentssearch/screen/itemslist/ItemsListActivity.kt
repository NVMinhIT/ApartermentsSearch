package vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist

import android.content.Intent
import android.os.Bundle

import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_items_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Util
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepositoryImpl
import vnjp.monstarlaplifetime.apartmentssearch.screen.detailroom.DetailRoomActivity
import vnjp.monstarlaplifetime.apartmentssearch.screen.guests.DateRangPickerBottomSheet
import vnjp.monstarlaplifetime.apartmentssearch.screen.guests.GuestsFragmentBottomSheet
import vnjp.monstarlaplifetime.apartmentssearch.screen.map.MapsActivity


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
        // test repository

        CoroutineScope(Dispatchers.Main).launch {
            val roomDatabaseReference =
                FirebaseDatabase.getInstance().getReference("rooms")
            val roomRepository = RoomRepositoryImpl(roomDatabaseReference)

            roomRepository.getRooms(
                onDataLoaded = {
                    Log.d("firebase", "on data load ${it.size}")
                },
                onException = {
                    Log.d("firebase", "on data load $it")
                })
        }
    }

    private fun initView() {
        recyclerView = findViewById(R.id.rvListItems)
        buttonOpenMap = findViewById(R.id.imbFeather)
        btCalendar = findViewById(R.id.btCalendar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        itemsListAdapter = ItemsListAdapter(this) {
            val intent = Intent(this, DetailRoomActivity::class.java)
            intent.putExtra(BUNDLE_ID, itemsListAdapter.getPosition(it).id)
            startActivity(intent)
        }
        recyclerView.adapter = itemsListAdapter
        val room = Util.getStatisticRooms()
        val arrayList = arrayListOf(room, room, room, room, room)
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
