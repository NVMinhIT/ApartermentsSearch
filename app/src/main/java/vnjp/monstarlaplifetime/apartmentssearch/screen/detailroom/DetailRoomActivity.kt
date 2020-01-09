package vnjp.monstarlaplifetime.apartmentssearch.screen.detailroom

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail_room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.NearbyLandmark
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Rent
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room
import vnjp.monstarlaplifetime.apartmentssearch.data.model.TotalGuest
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepositoryImpl
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepository
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.UserRepositoryImpl
import vnjp.monstarlaplifetime.apartmentssearch.screen.adapter.AmenitiesAdapter
import vnjp.monstarlaplifetime.apartmentssearch.screen.adapter.NearByLandAdapter
import vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist.ItemsListAdapter
import vnjp.monstarlaplifetime.apartmentssearch.utils.app.CacheManager

@Suppress("UsePropertyAccessSyntax")
class DetailRoomActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    lateinit var toolbar: Toolbar
    private lateinit var toolbarTitle: TextView
    private lateinit var geocoder: Geocoder
    private var mLastKnownLocation: Location? = null
    private lateinit var animationDrawable: AnimationDrawable
    private lateinit var imgRoomShow: ImageView
    var locations: List<LatLng> = java.util.ArrayList()
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationPermissionsGranted = false
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var amenitiesAdapter: AmenitiesAdapter
    private lateinit var recyclerView: RecyclerView
    private var idRoom: String? = null
    private lateinit var databaseReference: DatabaseReference
    private var listAmenties: MutableList<String> = mutableListOf()
    private lateinit var nearByLandAdapter: NearByLandAdapter
    private lateinit var recyclerViewNearBy: RecyclerView
    private var listNearBy: MutableList<NearbyLandmark> = mutableListOf()
    private lateinit var room: Room
    private lateinit var tvNumberDayNight: TextView
    private lateinit var btPriceRoom: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseReferenceBook: DatabaseReference
    lateinit var userRepository: UserRepository
    lateinit var tvBookNow: TextView
    private var latitude: Double? = null
    private var longtitude: Double? = null

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        setContentView(R.layout.activity_detail_room)
        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        room = Room()
        databaseReference = FirebaseDatabase.getInstance().getReference("rooms")
        firebaseAuth = FirebaseAuth.getInstance()
        userRepository =
            UserRepositoryImpl(FirebaseDatabase.getInstance().getReference("users"), firebaseAuth)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent.extras?.let {
            idRoom = it.getString(ItemsListAdapter.BUNDLE_ID_ROOM)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val roomRepository = RoomRepositoryImpl(databaseReference)
            idRoom?.let {
                roomRepository.getDetailRoom(
                    id = it,
                    onDataLoaded = {
                        room = it
                        Log.d("MINHMOM", room.name.toString())
                        initData()
                        mapFragment.getMapAsync(this@DetailRoomActivity)
                    },
                    onException = {
                        Log.d("firebase", "on data load $it")
                    })
            }

        }
        initView()
        initEvent()
    }

    private fun initData() {
        latitude = room.address?.latitude
        longtitude = room.address?.longtitude
        tvNameRoomDetail.setText(room.name.toString())
        tvAddress.setText(room.address?.address_name)
        room.comments?.size?.let { tvNumberReviews.setText(it.toString()) }
        room.comments?.size?.let { tvNumberRating.setText(it.toString()) }
        tvAddressMap.setText(room.address?.address_name)
        tvContentCancellation.setText(room.cancellations)
        listAmenties.clear()
        room.amenities?.let { listAmenties.addAll(it) }
        amenitiesAdapter.setListAmenities(listAmenties)
        listNearBy.clear()
        room.nearby_landmark?.let { listNearBy.addAll(it) }
        nearByLandAdapter.setListNearbyLandmark(listNearBy)
        val day = CacheManager.cacheManager?.getAccountDay()
        val numberDay = "<b>${day}</b> night stay "
        val priceAday: Int = room.price!!.toInt()
        val price: Int = day!!.toInt()
        val priceRoom: Int = priceAday * price
        tvNumberDayNight.setText(Html.fromHtml(numberDay, Html.FROM_HTML_MODE_LEGACY))
        val PriceDay = StringBuffer()
        PriceDay.append("$").append(priceRoom.toString())
        btPriceRoom.setText(PriceDay)
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        toolbarTitle = toolbar.findViewById(R.id.textToolbarTitle)
        imgRoomShow = findViewById(R.id.imgRoomShow)
        tvNumberDayNight = findViewById(R.id.tvNumberDayNight)
        tvBookNow = findViewById(R.id.tvBookNow)
        animationDrawable = imgRoomShow.drawable as AnimationDrawable
        animationDrawable.start()
        recyclerView = findViewById(R.id.rvListAmenities)
        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.setHasFixedSize(true)
        amenitiesAdapter = AmenitiesAdapter(this)
        recyclerView.adapter = amenitiesAdapter
        btPriceRoom = findViewById(R.id.btPriceRoom)
        val tvContentRule: TextView = findViewById(R.id.tvContentRule)
        tvContentRule.setLineSpacing(3F, 1.5f)
        toolbar.setNavigationIcon(R.drawable.ic_feather_arrow_left__white)
        toolbar.inflateMenu(R.menu.menu_detail)
        //nearbyLandmark
        recyclerViewNearBy = findViewById(R.id.rvListDistance)
        recyclerViewNearBy.layoutManager = LinearLayoutManager(this)
        nearByLandAdapter = NearByLandAdapter(this)
        recyclerViewNearBy.adapter = nearByLandAdapter

    }

    @SuppressLint("SetTextI18n")
    private fun initEvent() {
        // event AppBarLayout
        AppBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            @SuppressLint("ResourceAsColor")
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                when (state) {
                    State.COLLAPSED -> {
                        toolbarTitle.visibility = View.VISIBLE
                        toolbar.setBackgroundColor(getColor(R.color.color_text_white))
                        toolbar.setNavigationIcon(R.drawable.ic_feather_arrow_left__dark)
                        toolbarTitle.setText(room.name.toString())
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.setStatusBarColor(
                            ContextCompat.getColor(
                                this@DetailRoomActivity,
                                R.color.color_text_white
                            )
                        );
                    }
                    else -> {
                        toolbarTitle.visibility = View.INVISIBLE
                        toolbar.background = null
                        toolbar.setNavigationIcon(R.drawable.ic_feather_arrow_left__white)
                    }
                }
            }

        })
        toolbar.setNavigationOnClickListener {
            finish()
        }
        val userKey = FirebaseAuth.getInstance().currentUser?.uid
        val totalGuest: TotalGuest = CacheManager.cacheManager!!.getTotalGuest()
        val dataCheckIn = CacheManager.cacheManager!!.getCheckInDate()
        val dataCheckOut = CacheManager.cacheManager!!.getCheckOutDate()
        val rent = Rent(dataCheckIn, dataCheckOut, totalGuest)
        tvBookNow.setOnClickListener {

            userRepository.addRent(
                userKey!!,
                idRoom!!,
                rent,
                onAddResponse = { isComplete, message ->
                    if (isComplete) {
                        finish()
                        Toast.makeText(this, "BookRoom SuccessFull, Thanks", Toast.LENGTH_LONG)
                            .show()
                        Log.d("errow", message)
                    } else {
                        Toast.makeText(this, "Book Room not SuccessFull", Toast.LENGTH_LONG).show()
                    }

                })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.heart -> {
                //Toast.makeText(this@DetailRoomActivity, "Yêu thích", Toast.LENGTH_SHORT).show()
                Log.d("hihi", "HIHI")
                return true
            }
            R.id.share -> {
                Toast.makeText(this@DetailRoomActivity, "Chia sẻ", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        checkPermisstion()
    }

    private fun getDeviceLocation() {
        val address: LatLng = LatLng(latitude!!, longtitude!!)
        mMap.addMarker(MarkerOptions().position(address).title("Position"))
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(address, 13f)
        mMap.animateCamera(cameraUpdate)
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkPermisstion() {
        //version hien tai cua may
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isAccept = ActivityCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            //check xem thu permision da dc chap nhan chua
            if (isAccept == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            } else {
                startMap()
            }
        } else {
            startMap()
        }
    }

    private fun startMap() {
        getDeviceLocation()
        mMap.setMyLocationEnabled(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                return
            } else {
                startMap()
            }
            else -> startMap()
        }
    }
}
