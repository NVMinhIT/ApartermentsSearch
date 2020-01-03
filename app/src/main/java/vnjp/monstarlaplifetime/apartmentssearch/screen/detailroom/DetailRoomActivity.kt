package vnjp.monstarlaplifetime.apartmentssearch.screen.detailroom

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
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
import com.google.android.gms.tasks.Task
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_detail_room.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.screen.adapter.AmenitiesAdapter

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

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_room)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initView()
        initEvent()
    }
    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        toolbarTitle = toolbar.findViewById(R.id.textToolbarTitle)
        imgRoomShow = findViewById(R.id.imgRoomShow)
        animationDrawable = imgRoomShow.drawable as AnimationDrawable
        animationDrawable.start()
        mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(this@DetailRoomActivity)
        recyclerView = findViewById(R.id.rvListAmenities)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.setHasFixedSize(true)
        amenitiesAdapter = AmenitiesAdapter(this)
        recyclerView.adapter = amenitiesAdapter
        val tvContentRule: TextView = findViewById(R.id.tvContentRule)
        tvContentRule.setLineSpacing(3F, 1.5f)
        toolbar.setNavigationIcon(R.drawable.ic_feather_arrow_left__white)
        toolbar.inflateMenu(R.menu.menu_detail)
    }
    @SuppressLint("SetTextI18n")
    private fun initEvent() {
        toolbarTitle.setText("Cozy Victorian Apartment in Islington")
        AppBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            @SuppressLint("ResourceAsColor")
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                when (state) {
                    State.COLLAPSED -> {
                        toolbarTitle.visibility = View.VISIBLE
                        toolbar.setBackgroundColor(getColor(R.color.color_text_white))
                        toolbar.setNavigationIcon(R.drawable.ic_feather_arrow_left__dark)
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
        val arr: ArrayList<String> = arrayListOf(
            "TV",
            "Washer",
            "Coffee maker",
            "TV",
            "Washer",
            "Wifi"
        )
        amenitiesAdapter.setListAmenities(arr)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.heart -> {
                Toast.makeText(this, "Yêu thích", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.share -> {
                Toast.makeText(this, "Chia sẻ", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney: LatLng = LatLng((-34).toDouble(), 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13f), 2000, null)
        // geocoder = Geocoder(this@DetailRoomActivity)
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isMyLocationButtonEnabled = true
        //getCurrentLocationNoMove()
        //checkPermisstion()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun checkPermisstion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isAccept = ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (isAccept == PackageManager.PERMISSION_DENIED) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            } else {
                //startMap()
            }
        } else {
            //startMap()
        }
    }


    private fun getDeviceLocation() {
        val locationResult: Task<Location>? =
            mFusedLocationProviderClient?.getLastLocation()
        locationResult?.addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                mLastKnownLocation = task.result
                val target = LatLng(
                    mLastKnownLocation!!.latitude
                    , mLastKnownLocation!!.longitude
                )
                val mark = MarkerOptions()
                    .title("Position.")
                    .position(target)
                mMap.addMarker(mark)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, 13f)
                mMap.animateCamera(cameraUpdate)
            } else {
                Toast.makeText(this@DetailRoomActivity, "problem here", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
