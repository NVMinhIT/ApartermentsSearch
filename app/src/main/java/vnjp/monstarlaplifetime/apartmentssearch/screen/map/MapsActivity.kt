package vnjp.monstarlaplifetime.apartmentssearch.screen.map

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_maps.*
import vnjp.monstarlaplifetime.apartmentssearch.R
import vnjp.monstarlaplifetime.apartmentssearch.data.model.Room
import vnjp.monstarlaplifetime.apartmentssearch.data.repository.RoomRepositoryImpl
import vnjp.monstarlaplifetime.apartmentssearch.screen.detailroom.DetailRoomActivity
import vnjp.monstarlaplifetime.apartmentssearch.screen.itemslist.ItemsListAdapter


class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var location: Location? = null
    private var currentMarker: Marker? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // repo
    private val roomDatabaseReference = FirebaseDatabase.getInstance().getReference("rooms")
    private val roomRepository = RoomRepositoryImpl(roomDatabaseReference)

    lateinit var buttonClose: ImageButton
    lateinit var cardRoomReview: ConstraintLayout

    companion object {
        const val REQUEST_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        init()
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_PERMISSION_CODE
            )
        } else {
            getLastLocation()
        }
    }

    private fun init() {
        buttonClose = findViewById(R.id.buttonClose)
        cardRoomReview = findViewById(R.id.reviewContainer)
        cardRoomReview.visibility = View.INVISIBLE
        buttonClose.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            location = it
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLastLocation()
                } else {
                    Log.d("location", "Permission has been denied")
                }
                return
            }
            else -> {
                Log.d("location", "Permission has been denied")
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLoadedCallback {
            location?.let {
                val current = LatLng(it.latitude, it.longitude)
                val builder = LatLngBounds.Builder()
                builder.include(current)
                mMap.addMarker(
                    MarkerOptions()
                        .position(current)
                ).tag = "you"
                roomRepository.getRoomInRange(current, 10.toDouble(), onDataLoaded = { rooms ->
                    for ((key, room) in rooms) {
                        val mark = LatLng(
                            room.address?.latitude!!,
                            room.address?.longtitude!!
                        )
                        mMap.addMarker(
                            MarkerOptions()
                                .position(mark)
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        createMarker(
                                            R.layout.layout_map_marker,
                                            room.price
                                        )
                                    )
                                )
                        ).tag = key

                    }
                    val bounds = builder.build()
                    val cu = CameraUpdateFactory.newLatLngBounds(bounds, 200)
                    mMap.moveCamera(cu)
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17f), 2000, null)
                }, onException = { message -> Log.d("location", message) })
            }
        }

        mMap.setOnMarkerClickListener { marker ->
            val roomKey: String = marker.tag as String
            if (roomKey != "you") {
                roomRepository.getDetailRoom(roomKey, onException = {}, onDataLoaded = { room ->
                    if (currentMarker == null) {
                        currentMarker = marker
                    } else {
                        currentMarker!!.setIcon(
                            BitmapDescriptorFactory.fromBitmap(
                                createMarker(
                                    R.layout.layout_map_marker,
                                    room.price
                                )
                            )
                        )
                    }
                    marker.setIcon(
                        BitmapDescriptorFactory.fromBitmap(
                            createMarker(
                                R.layout.layout_map_marker_selected,
                                room.price
                            )
                        )
                    )
                    previewRoom(room, roomKey)
                    currentMarker = marker
                })

            }
            false
        }

    }

    private fun createMarker(layout: Int, price: Float?): Bitmap {
        val marker = LayoutInflater.from(this).inflate(layout, null)
        val text: TextView = marker.findViewById(R.id.textMarkerPrice)
        text.text = price.toString()
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildLayer()
        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }

    private fun previewRoom(room: Room, key: String) {
        cardRoomReview.visibility = View.VISIBLE
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(CenterCrop(), RoundedCorners(22))
        Glide.with(this).load(room.image).apply(requestOptions).into(imageRoom)
        textRoomName.text = room.name
        textPrice.text = room.price.toString()
        cardRoomReview.setOnClickListener {
            val intent = Intent(this, DetailRoomActivity::class.java)
            intent.putExtra(ItemsListAdapter.BUNDLE_ID_ROOM, key)
            startActivity(intent)
        }
    }

}
